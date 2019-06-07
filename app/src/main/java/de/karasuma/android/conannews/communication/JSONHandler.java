package de.karasuma.android.conannews.communication;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.karasuma.android.conannews.MainActivity;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;

class JSONHandler {
    public static JSONArray createJSONArrayFromConnection(HttpsURLConnection con) {
        JSONArray jsonArray = null;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
            jsonArray = new JSONArray(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static void createPosts(JSONArray jsonArray, MainActivity mainActivity) {
        for (int i = 0; i < 10; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getJSONObject("title").getString("rendered");
                title = Jsoup.parse(title).text();

                String content = jsonObject.getJSONObject("content").getString("rendered");
                content = Jsoup.parse(content).text();

                Post post = new Post(title, content);
                Model.getInstance().getPosts().add(post);

                RequestPostCoverTask task = new RequestPostCoverTask(mainActivity, post);
                task.execute(jsonObject.getJSONObject("_links")
                        .getJSONArray("wp:featuredmedia")
                        .getJSONObject(0)
                        .getString("href"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject createJSONObjectFromConnection(HttpsURLConnection con) {
        JSONObject jsonObject = null;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
            jsonObject = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
