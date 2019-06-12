package de.karasuma.android.conannews.communication.html;
import android.content.Intent;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

import de.karasuma.android.conannews.MainActivity;
import de.karasuma.android.conannews.PostActivity;
import de.karasuma.android.conannews.data.Post;

public class OpenPostTask extends AsyncTask<URL, Integer, Post> {
    private final MainActivity mainActivity;
    private Post post;

    public OpenPostTask(Post post, MainActivity mainActivity) {
        this.post = post;
        this.mainActivity = mainActivity;
    }

    @Override
    protected Post doInBackground(URL... urls) {

        //if post has already been initialized use values
        if (post.getContent() != null) {
            return post;
        }

        try {
            Document doc = Jsoup.connect(post.getUrl()).get();
            Element articleElement = doc.body().select("article").first();
            post = HTMLParser.parseArticle(articleElement, post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    protected void onPostExecute(Post post) {
        super.onPostExecute(post);
        Intent intent = new Intent(mainActivity, PostActivity.class);
        intent.putExtra("content", post.getContent());
        intent.putExtra("title", post.getTitle());
        mainActivity.startActivity(intent);
    }
}
