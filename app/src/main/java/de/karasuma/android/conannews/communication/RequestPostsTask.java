package de.karasuma.android.conannews.communication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RequestPostsTask extends AsyncTask<URL, JSONArray, JSONArray> {

    private String url = "https://conannews.org/wp-json/wp/v2/posts/";

    @Override
    protected JSONArray doInBackground(URL[] urls) {
        HttpsURLConnection con = HTTPRequester.makeHTTPRequest(url);

        if (con == null) {
            return null;
        }

        JSONObject jsonObject = JSONHandler.createJSONFromConnection(con);
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        JSONHandler.createPosts(jsonArray);
    }
}
