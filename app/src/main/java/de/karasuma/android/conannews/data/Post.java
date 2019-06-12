package de.karasuma.android.conannews.data;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;

public class Post {
    private String author;
    private String published;
    private String summary;
    private String title;
    private String content;
    private Bitmap bitmap;
    private String url;
    private ArrayList<Category> categories = new ArrayList<>();

    public Post(String title, String date, String author, String summary) {
        this.title = title;
        this.published = date;
        this.author = author;
        this.summary = summary;

        this.summary = summary;
    }

    public Post() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
