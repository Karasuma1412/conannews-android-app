package de.karasuma.android.conannews.communication.wpapi;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPRequester {

    private String baseURL = "https://conannews.org/wp-json/";
    private String postRequestString = "wp/v2/posts/";

    public static HttpsURLConnection makeHTTPRequest(String urlString) {
        HttpsURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("GET");
            con.connect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return con;
    }

}
