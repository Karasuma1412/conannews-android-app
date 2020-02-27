package de.karasuma.android.conannews.communication.html;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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

import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.activities.PostActivity;
import de.karasuma.android.conannews.communication.LinkHandler;
import de.karasuma.android.conannews.data.Category;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;
import de.karasuma.android.conannews.filehandling.ConanCastFileController;

class HTMLParser {

    private static String TAG = "HTMLParser";

    public static Post parsePost(final Element element) {
        final Post post = new Post();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.v(TAG, "parsing thumbnail of " + post.getTitle());
                    post.setBitmap(parseThumbnail(element));
                    Log.v(TAG, "finished parsing thumbnail of " + post.getTitle());
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        post.setTitle(parseTitle(element));
        post.setPublished(parsePublished(element));
        post.setAuthor(parseAuthor(element));
        post.setSummary(parseSummary(element));
        post.setUrl(parseURL(element));
        post.setCategories(parseCategory(element, post));
        parseCategoryColorAndFilterURL(element, post);

//        OpenPostTask task = new OpenPostTask(post);
//        task.execute();

        return post;
    }

    private static void parseCategoryColorAndFilterURL(Element element, Post post) {
        Elements categoryElements = element.getElementsByAttributeValue("rel", "category TAG");
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
        Elements categoryElements = element.getElementsByAttributeValue("rel", "category TAG");
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
        LinearLayout view = (LinearLayout) postActivity.getLayoutInflater().inflate(R.layout.article_layout, null, false);
        view.setOrientation(LinearLayout.VERTICAL);

        //get article TAG
        LinearLayout categories = new LinearLayout(postActivity);
        Elements categoriesElement = articleElement.getElementsByAttributeValue("rel", "category TAG");
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

        Element datePublished = articleElement.getElementsByClass("entry-date published").first();
        if (datePublished == null) {
            datePublished = articleElement.getElementsByClass("entry-date published updated").first();
        }
        String published = datePublished.text();
        publishedView.setText(published);

        String author = articleElement.getElementsByClass("url fn n").first().text();
        authorView.setText(author);

        view.addView(articleInfoLayout);

        //content
        LinearLayout contentLayout = (LinearLayout) postActivity.getLayoutInflater().inflate(R.layout.article_content, view, false);
        Element contentElements = articleElement.getElementsByClass("entry-content clearfix").first();
        Elements paragraphElements = contentElements.select("p, h1, h2, h3, h4, h5, h6, table");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        for (Element e : paragraphElements) {

            if (!e.hasText()) {
                continue;
            }

            SpannableString spannableString = new SpannableString(e.text());
            Log.v(TAG, "paragraph element string: " + spannableString.toString());

            for (Element img : e.select("img")) {
                String imageURLString = img.absUrl("src");
                try {
                    /*
                    force image urls to use https
                     */
                    if (!imageURLString.contains("https")) {
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
            for (final Element link : e.select("a")) {
                final String url = link.absUrl("href");
                String linkText = link.text();
                int startIndex = e.text().indexOf(linkText);
                int endIndex = startIndex + linkText.length();

                ClickableSpan linkClickableSpan = new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        LinkHandler linkHandler = new LinkHandler();
                        linkHandler.open(link, url, postActivity);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(postActivity.getResources().getColor(R.color.colorLink));
                        ds.setUnderlineText(true);
                    }
                };
                Log.v(TAG, link.absUrl("href"));
                Log.v(TAG, "startIndex: " + startIndex + ", endIndex: " + endIndex);
                spannableString.setSpan(linkClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (e.is("table")) {
                Log.v(TAG, "element is table");
                TableLayout tableLayout = (TableLayout) postActivity.getLayoutInflater().inflate(R.layout.article_table, contentLayout, false);
                for (Element rowElement : e.select("tr")) {
                    TableRow row = (TableRow) postActivity.getLayoutInflater().inflate(R.layout.article_table_row, tableLayout, false);
                    for (Element colElement : rowElement.select("td")) {
                        TextView col = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_table_content_text, row, false);
                        col.setText(colElement.text());

                        for (final Element link : colElement.select("a")) {
                            SpannableString tableSpanString = new SpannableString(colElement.text());
                            Log.v(TAG, "Link found: " + link.text());
                            final String url = link.absUrl("href");
                            String linkText = link.text();
                            int startIndex = colElement.text().indexOf(linkText);
                            int endIndex = startIndex + linkText.length();

                            ClickableSpan linkClickableSpan = new ClickableSpan() {

                                @Override
                                public void onClick(View widget) {
                                    LinkHandler linkHandler = new LinkHandler();
                                    linkHandler.open(link, url, postActivity);
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(postActivity.getResources().getColor(R.color.colorLink));
                                    ds.setUnderlineText(true);
                                }
                            };
                            Log.v(TAG, "startIndex: " + startIndex + ", endIndex: " + endIndex);
                            tableSpanString.setSpan(linkClickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            col.setText(tableSpanString);
                            col.setMovementMethod(LinkMovementMethod.getInstance());
                        }

                        //create border
                        ShapeDrawable border = new ShapeDrawable(new RectShape());
                        border.getPaint().setStyle(Paint.Style.STROKE);
                        border.getPaint().setColor(Color.BLACK);
                        col.setBackground(border);
                        row.addView(col);
                    }
                    tableLayout.addView(row);
                }
                contentLayout.addView(tableLayout);
            } else if (e.is("h1, h2, h3, h4, h5, h6")) {
                Log.v(TAG, "element is subheadline");

                TextView subheadline = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_subheadline, contentLayout, false);
                subheadline.setText(spannableString);
                subheadline.setMovementMethod(LinkMovementMethod.getInstance());
                contentLayout.addView(subheadline);
            } else {
                Log.v(TAG, "element is paragraph");

                TextView paragraphView = (TextView) postActivity.getLayoutInflater().inflate(R.layout.article_paragraph, contentLayout, false);
                paragraphView.setText(spannableString);
                paragraphView.setMovementMethod(LinkMovementMethod.getInstance());
                contentLayout.addView(paragraphView);
            }
        }

        view.addView(contentLayout);

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
