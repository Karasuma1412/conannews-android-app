package de.karasuma.android.conannews.data;

import android.graphics.Bitmap;

public class Post {
    private String title;
    private String content;
    private Bitmap bitmap;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
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
}
