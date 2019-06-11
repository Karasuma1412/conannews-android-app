package de.karasuma.android.conannews.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.karasuma.android.conannews.MainActivity;
import de.karasuma.android.conannews.data.Post;

class RequestPostCoverTask extends AsyncTask<String, Integer, Bitmap> {

    private final MainActivity mainActivity;
    private final Post post;

    public RequestPostCoverTask(MainActivity mainActivity, Post post) {
        this.mainActivity = mainActivity;
        this.post = post;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        HttpsURLConnection httpsURLConnection = HTTPRequester.makeHTTPRequest(urls[0]);

        if (httpsURLConnection == null) {
            return null;
        }

        JSONObject jsonObject = JSONHandler.createJSONObjectFromConnection(httpsURLConnection);
        URL url = null;
        try {
            url = new URL(jsonObject
                    .getJSONObject("media_details")
                    .getJSONObject("sizes")
                    .getJSONObject("thumbnail")
                    .getString("source_url"));
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        post.setBitmap(bitmap);
        mainActivity.updatePosts();
    }
}
