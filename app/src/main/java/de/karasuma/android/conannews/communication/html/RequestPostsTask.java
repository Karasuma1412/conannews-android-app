package de.karasuma.android.conannews.communication.html;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import de.karasuma.android.conannews.MainActivity;

public class RequestPostsTask extends AsyncTask<String, Integer, JSONArray> {

    private final MainActivity mainActivity;
    private String conanNewsUrl = "https://conannews.org/";

    public RequestPostsTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {
        try {
            Document doc = Jsoup.connect(conanNewsUrl).get();
            Elements elements = doc.body().select("article");

            for (Element element : elements) {
                HTMLParser.parsePost(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        mainActivity.getProgressCircular().setVisibility(View.INVISIBLE);
        mainActivity.updatePosts();
    }
}
