package de.karasuma.android.conannews.data;

import android.graphics.Bitmap;

public class Post {
    private String title;
    private String content;
    private Bitmap bitmap;

    public Post(String title, String content, Bitmap bitmap) {
        this.title = title;
        this.content = content;
        this.bitmap = bitmap;
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
}
