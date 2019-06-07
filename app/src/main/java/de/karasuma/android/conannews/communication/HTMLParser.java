package de.karasuma.android.conannews.communication;

class HTMLParser {
    public static String parseToString(String content) {
        String result = content.replaceAll("\\<.*?>","");
        return result;
    }
}
