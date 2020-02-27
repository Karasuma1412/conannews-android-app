package de.karasuma.android.conannews.communication.html;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.karasuma.android.conannews.activities.MainActivity;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;

public class RequestPostsTask extends AsyncTask<String, Integer, List<Post>> {

    private final MainActivity mainActivity;
    private String conanNewsUrl = "https://conannews.org/";
    private final String tag = "RequestPostsTask_HTML";

    public RequestPostsTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Post> doInBackground(String... strings) {
        LinkedList<Post> posts = new LinkedList<>();
        try {
            String url = Model.getInstance().getUrlUtil().createHttpsURL(strings[0]);
            Log.v(tag, url);
            Connection.Response response = Jsoup.connect(url).execute();
            mainActivity.setConanNewsURL(response.url().toString());;

            Document doc = response.parse();
            Elements elements = doc.body().select("article");

            for (Element element : elements) {
                Log.v(tag, "parsing post");
                posts.add(HTMLParser.parsePost(element));
                publishProgress(elements.indexOf(element));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        super.onPostExecute(posts);
        Model.getInstance().getPosts().addAll(posts);
        mainActivity.getProgressCircular().setVisibility(View.INVISIBLE);
        mainActivity.updatePosts();
    }
}
