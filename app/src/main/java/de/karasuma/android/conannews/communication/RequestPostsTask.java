package de.karasuma.android.conannews.communication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.karasuma.android.conannews.MainActivity;

public class RequestPostsTask extends AsyncTask<URL, JSONArray, JSONArray> {

    private final MainActivity mainActivity;
    private String url = "https://conannews.org/wp-json/wp/v2/posts/";

    public RequestPostsTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected JSONArray doInBackground(URL[] urls) {
        HttpsURLConnection con = HTTPRequester.makeHTTPRequest(url);

        if (con == null) {
            return null;
        }

        JSONArray jsonArray = JSONHandler.createJSONFromConnection(con);
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        JSONHandler.createPosts(jsonArray);
        mainActivity.updatePosts();
    }
}
