package de.karasuma.android.conannews.data;

import android.graphics.Bitmap;

public class Post {
    private String author;
    private String published;
    private String summary;
    private String title;
    private String content;
    private Bitmap bitmap;

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
}
