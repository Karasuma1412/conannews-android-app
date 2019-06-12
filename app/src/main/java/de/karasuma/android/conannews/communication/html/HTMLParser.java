package de.karasuma.android.conannews.communication.html;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;

class HTMLParser {
    public static void parsePost(Element element) {
        Post post = new Post();
        post.setBitmap(parseThumbnail(element));
        post.setTitle(parseTitle(element));
        post.setPublished(parsePublished(element));
        post.setAuthor(parseAuthor(element));
        post.setSummary(parseSummary(element));
        post.setUrl(parseURL(element));

//        OpenPostTask task = new OpenPostTask(post);
//        task.execute();

        Model.getInstance().getPosts().add(post);
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

    private static Bitmap parseThumbnail(Element element) {
        Elements featuredImage = element.getElementsByClass("featured-image");
        Element image = featuredImage.select("img").first();
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

    public static Post parseArticle(Element articleElement, Post post) {
        Elements paragraphs = articleElement.select("p");
        StringBuilder builder = new StringBuilder();
        for (Element paragraphElement : paragraphs) {
            builder.append(paragraphElement.text() + "\n\n");
        }
        post.setContent(builder.toString());
        return post;
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
