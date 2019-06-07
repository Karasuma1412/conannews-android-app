package de.karasuma.android.conannews.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.karasuma.android.conannews.MainActivity;

public class RequestPostsTask extends AsyncTask<URL, JSONArray, JSONArray> {

    ArrayList<Bitmap> bitmaps = new ArrayList();

    private final MainActivity mainActivity;
    private String url = "https://conannews.org/wp-json/wp/v2/posts/";

    public RequestPostsTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static void requestThumbnail() {

    }

    @Override
    protected JSONArray doInBackground(URL[] urls) {
        HttpsURLConnection con = HTTPRequester.makeHTTPRequest(url);

        if (con == null) {
            return null;
        }

        JSONArray jsonArray = JSONHandler.createJSONArrayFromConnection(con);
        bitmaps = new ArrayList();

        //create post thumbnails
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String href = jsonObject.getJSONObject("_links")
                        .getJSONArray("wp:featuredmedia")
                        .getJSONObject(0)
                        .getString("href");

                HttpsURLConnection httpsURLConnection = HTTPRequester.makeHTTPRequest(href);

                if (httpsURLConnection == null) {
                    return null;
                }

                jsonObject = JSONHandler.createJSONObjectFromConnection(httpsURLConnection);
                URL url = new URL(jsonObject.getString("source_url"));


                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmaps.add(bmp);
                Log.v("RequestPostsTask", bmp.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        JSONHandler.createPosts(jsonArray, bitmaps);
        mainActivity.updatePosts();
    }
}
