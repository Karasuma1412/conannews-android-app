package de.karasuma.android.conannews.util;

public class URLUtil {
    public String createHttpsURL(String url) {
        if (!url.contains("https")) {
            return url.replace("http", "https");
        }
        return url;
    }
}
