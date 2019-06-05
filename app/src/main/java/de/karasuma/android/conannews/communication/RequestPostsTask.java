package de.karasuma.android.conannews.communication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RequestPostsTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private String url = "https://conannews.org/wp-json/wp/v2/posts/";

    @Override
    protected JSONObject doInBackground(URL[] urls) {
        HttpsURLConnection con = HTTPRequester.makeHTTPRequest(url);

        if (con == null) {
            return null;
        }

        JSONObject jsonObject = JSONHandler.createJSONFromConnection(con);
        JSONArray array;
        jsonObject.toJSONArray("array");
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        JSONArray array = jsonObject.toJSONArray(jsonObject);
        JSONHandler.createPosts(jsonObject);
    }
}
