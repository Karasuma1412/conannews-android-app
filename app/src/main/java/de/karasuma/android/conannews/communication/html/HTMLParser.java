package de.karasuma.android.conannews.communication.html;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.karasuma.android.conannews.PostActivity;
import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;
import de.karasuma.android.conannews.data.Category;

class HTMLParser {

    private static String tag = "HTMLParser";

    public static void parsePost(Element element) {
        Post post = new Post();
        try {
            post.setBitmap(parseThumbnail(element));
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        post.setTitle(parseTitle(element));
        post.setPublished(parsePublished(element));
        post.setAuthor(parseAuthor(element));
        post.setSummary(parseSummary(element));
        post.setUrl(parseURL(element));
        post.setCategories(parseCategory(element, post));
        parseCategoryColorAndFilterURL(element, post);

//        OpenPostTask task = new OpenPostTask(post);
//        task.execute();

        Model.getInstance().getPosts().add(post);
    }

    private static void parseCategoryColorAndFilterURL(Element element, Post post) {
        Elements categoryElements = element.getElementsByAttributeValue("rel", "category tag");
        for (int i = 0; i < categoryElements.size(); i++) {
            Element categoryTag = categoryElements.get(i);
            String style = categoryTag.attr("style");
            String filterURL = categoryTag.attr("href");

            if (style.isEmpty()) {
                post.getCategories().get(i).setColor("#123456");
                return;
            }

            style = style.substring(11);
            post.getCategories().get(i).setColor(style);
            post.getCategories().get(i).setFilterURL(filterURL);
        }
    }

    private static ArrayList<Category> parseCategory(Element element, Post post) {
        ArrayList<Category> categories = new ArrayList<>();
        Elements categoryElements = element.getElementsByAttributeValue("rel", "category tag");
        for (Element e : categoryElements) {
            Category category = new Category();
            category.setName(e.text());
            categories.add(category);
        }

        return categories;
    }

    private static String parseURL(Element element) {
        Elements entryContentClearfix = element.getElementsByClass("entry-content clearfix");
        Element urlElement = entryContentClearfix.select("a").first();
        return urlElement.absUrl("href");
    }

    private static String parseSummary(Element element) {
        Elements entryContentClearfix = element.getElementsByClass("entry-content clearfix");
        Element summaryElement = entryContentClearfix.select("p").first();
        return summaryElement.text();
    }

    private static String parseAuthor(Element element) {
        Elements authorVCard = element.getElementsByClass("author vcard");
        Element authorElement = authorVCard.select("a").first();
        return authorElement.text();
    }

    private static String parsePublished(Element element) {
        Elements entryDatePublished = element.getElementsByClass("entry-date published");
        return entryDatePublished.text();
    }

    private static String parseTitle(Element element) {
        Elements entryTitle = element.getElementsByClass("entry-title");
        Element titleElement = entryTitle.select("a").first();
        String title = titleElement.text();
        return title;
    }

    private static Bitmap parseThumbnail(Element element) throws SerializationException {
        Elements featuredImage = element.getElementsByClass("featured-image");
        Element image = featuredImage.select("img").first();
        if (image == null) {
            throw new SerializationException("Article does not have a thumbnail");
        }
        String imageURLString = image.absUrl("src");
        try {
            URL imageURL = new URL(imageURLString);
            return BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static View parseArticle(Element articleElement, final PostActivity postActivity) {
        LinearLayout view = (LinearLayout) postActivity.getLayoutInflater().inflate(R.layout.article_layout, null, false);;
        view.setOrientation(LinearLayout.VERTICAL);

        //get article tag
        LinearLayout categories = new LinearLayout(postActivity);
        Elements categoriesElement = articleElement.getElementsByAttributeValue("rel", "category tag");
        for (Element e : categoriesElement) {
            CardView cardView;
            cardView = (CardView) postActivity.getLayoutInflater().inflate(R.layout.category_item, categories, false);

            String name = e.text();
            String style = e.attr("style");
            String color = style.substring(11);

            TextView categoryText = (TextView) cardView.getChildAt(0);
            categoryText.setText(name);
            categoryText.setBackgroundColor(Color.parseColor(color));
            categories.addView(cardView);
        }
        view.addView(categories);

        //get article title
        Element titleElement = articleElement.getElementsByClass("entry-title").first();
        String title = "";
        if (titleElement != null) {
            title = titleElement.text();
        }


        TextView titleView = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_title, view, false);
        titleView.setText(title);
        view.addView(titleView);

        //date and author
        LinearLayout articleInfoLayout = (LinearLayout) postActivity.getLayoutInflater().inflate(R.layout.article_info, view, false);
        TextView publishedView = (TextView) articleInfoLayout.getChildAt(0);
        TextView authorView = (TextView) articleInfoLayout.getChildAt(1);

        String published = articleElement.getElementsByClass("entry-date published").first().text();
        publishedView.setText(published);

        String author = articleElement.getElementsByClass("url fn n").first().text();
        authorView.setText(author);

        view.addView(articleInfoLayout);

        //content
        LinearLayout contentLayout = (LinearLayout) postActivity.getLayoutInflater().inflate(R.layout.article_content, view, false);
        Element contentElements = articleElement.getElementsByClass("entry-content clearfix").first();
        Elements paragraphElements = contentElements.select("p");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        for (Element e : paragraphElements) {
            SpannableString spannableString = new SpannableString(e.text());

            for (Element img : e.select("img")) {
                String imageURLString = img.absUrl("src");
                try {
                    /*
                    force image urls to use https
                     */
                    if(!imageURLString.contains("https")) {
                        imageURLString = imageURLString.replace("http", "https");
                    }

                    URL imageURL = new URL(imageURLString);
                    Bitmap image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    ImageView imageView = (ImageView) postActivity.getLayoutInflater().inflate(R.layout.article_image, null, false);
                    imageView.setImageBitmap(image);
                    contentLayout.addView(imageView);
                } catch (MalformedURLException exception) {
                    exception.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            for (Element link : e.select("a")) {
                final String url = link.absUrl("href");
                String linkText = link.text();
                int startIndex = e.text().indexOf(linkText);
                int endIndex = startIndex + linkText.length();

                ClickableSpan linkClickableSpan = new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        if (isConannewsLink(url)) {
                            Intent intent = new Intent(postActivity, PostActivity.class);
                            intent.putExtra("url", url);
                            postActivity.startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            postActivity.startActivity(intent);
                        }
                    }

                    private boolean isConannewsLink(String url) {
                        return url.contains("conannews.org");
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(postActivity.getResources().getColor(R.color.colorLink));
                        ds.setUnderlineText(true);
                    }
                };
                spannableString.setSpan(linkClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            TextView paragraphView = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_paragraph, null, false);
            paragraphView.setText(spannableString);
            paragraphView.setMovementMethod(LinkMovementMethod.getInstance());
            contentLayout.addView(paragraphView);
        }

        view.addView(contentLayout);

//        TextView paragraphView = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_paragraph, view, false);
//        paragraphView.setText(spannableStringBuilder);
//        paragraphView.setMovementMethod(LinkMovementMethod.getInstance());
//        view.addView(paragraphView);

//        for (Element element : contentElements.select("p")) {
//            System.out.println(element.html());
//            TextView contentView = new TextView(postActivity);
//            contentView.setText(element.text());
//            view.addView(contentView);
//        }

        //createArticle(articleElement, )



        Elements paragraphs = articleElement.select("p");
        StringBuilder builder = new StringBuilder();
        for (Element paragraphElement : paragraphs) {
            builder.append(paragraphElement.text() + "\n\n");
        }
        return view;
    }

    public void parsePosts(BufferedReader br) throws IOException {
        Log.v("HTMLParser", "start parsing");
        StringBuilder builder = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            builder.append(line);
        }
        String html = builder.toString();
        Document doc = Jsoup.parse(html, "", Parser.xmlParser());
        Elements elements = doc.body().select("article");

        for (Element element : elements) {
            Log.v("HTMLParser", element.html());
        }

//        try {
//            line = br.readLine();
//            while (line != null) {
//                if (isArticle(line)) {
//                    Log.v("HTMLParser", "found article");
//                    parseArticle(line, br);
//                    line = br.readLine();
//                }
//                line = br.readLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.v("HTMLParser", "end parsing");
    }

    private void parseArticle(String line, BufferedReader br) throws IOException {
        Post post = new Post();

        //skip to image
        while (line != null && !line.contains("<div class=\"featured-image\">")) {
            line = br.readLine();
        }
        line = br.readLine();
        line = br.readLine();
        System.out.println(line);

        Document doc = Jsoup.parse(line, "", Parser.xmlParser());
        Element image = doc.select("img").first();
        String url = image.absUrl("src");
        System.out.println(url);

        String title = Jsoup.parse(line).text();
        post.setTitle(title);

        //skip to publish date
        while (line != null && !line.contains("class=\"entry-date published\"")) {
            line = br.readLine();
        }
        String published = Jsoup.parse(line).text();
        post.setPublished(published);

        //skip to author
        while (line != null && !line.contains("href=\"https://conannews.org/author")) {
            line = br.readLine();
        }
        String author = Jsoup.parse(line).text();
        post.setAuthor(author);

        //skip to overview
        while (line != null && !line.contains("class=\"entry-content clearfix\"")) {
            line = br.readLine();
        }
        line = br.readLine();
        String summary = Jsoup.parse(line).text();
        post.setSummary(summary);

        Model.getInstance().getPosts().add(post);
    }

    private boolean isArticle(String line) {
        return line.contains("<article");
    }
}
