package de.karasuma.android.conannews.communication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

class JSONHandler {
    public static JSONObject createJSONFromConnection(HttpsURLConnection con) {
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

    public static void createPosts(JSONArray jsonArray) {
        for (int i = 0; i < 10; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getJSONObject("title").getString("rendered");
                String content = jsonObject.getJSONObject("content").getString("rendered");
                HTMLParser.parseToString(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
