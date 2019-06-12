package de.karasuma.android.conannews.communication.html;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import de.karasuma.android.conannews.PostActivity;
import de.karasuma.android.conannews.R;

public class OpenPostTask extends AsyncTask<String, Integer, View> {
    private final PostActivity postActivity;

    public OpenPostTask(PostActivity postActivity) {
        this.postActivity = postActivity;
    }

    @Override
    protected View doInBackground(String... urls) {
        View view = null;
        try {
            Document doc = Jsoup.connect(urls[0]).get();
            Element articleElement = doc.body().select("article").first();
            view = HTMLParser.parseArticle(articleElement, postActivity);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    protected void onPostExecute(View view) {
        super.onPostExecute(view);
        LinearLayout parent = postActivity.findViewById(R.id.parent);
        postActivity.getProgressCircle().setVisibility(View.INVISIBLE);
        parent.addView(view);
    }
}
