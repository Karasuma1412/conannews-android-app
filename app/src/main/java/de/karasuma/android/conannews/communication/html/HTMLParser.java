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
        Bitmap bmp = parseThumbnail(element);
        post.setBitmap(bmp);
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
