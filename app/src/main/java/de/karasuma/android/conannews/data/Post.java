package de.karasuma.android.conannews.data;

import android.graphics.Bitmap;

public class Post {
    private String date;
    private String summary;
    private String title;
    private String content;
    private Bitmap bitmap;

    public Post(String title, String content, String summary, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDate() {
        return date;
    }

    public String getSummary() {
        return summary;
    }
}
