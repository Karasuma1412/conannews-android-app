package de.karasuma.android.conannews.communication;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.nodes.Element;

import java.util.Scanner;

import de.karasuma.android.conannews.activities.MainActivity;
import de.karasuma.android.conannews.activities.PostActivity;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.filehandling.ConanCastFileController;

public class LinkHandler {
    private static final String TAG = "LinkHandler";

    public void open(Element linkElement, String url, AppCompatActivity activity) {
        if (isConanCastDownloadLink(linkElement)) {
            startDownload(url, activity);
        } else if (isConanNewsLink(url)) {
            openConanNewsLink(url, activity);
        } else {
            openInBrowser(url, activity);
        }
    }

    private void openInBrowser(String url, AppCompatActivity activity) {
        Log.v(TAG, "External link...");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    private void openConanNewsLink(String url, AppCompatActivity activity) {
        url = Model.getInstance().getUrlUtil().createHttpsURL(url);
        Log.v(TAG, "Handle internal link: " + url);
        if (isConanNewsArticleLink(url)) {
            Log.v(TAG, "ConanNews article link");
            Intent intent = new Intent(activity, PostActivity.class);
            intent.putExtra("url", url);
            activity.startActivity(intent);
        } else if (isConanNewsSearchLink(url)) {
            Log.v(TAG, "ConanNews search link");
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("filterURL", url);
            activity.startActivity(intent);
        } else {
            Log.v(TAG, "ConanNews other link");
            //open in web view
        }

    }

    private boolean isConanNewsSearchLink(String url) {
        return url.contains("category");
    }

    private boolean isConanNewsArticleLink(String url) {
        String[] splits = url.split("/");
        if (splits.length != 4) {
            return false;
        }

        Log.v(TAG, "Splits: " + splits.length);
        for (String split : splits) {
            Log.v(TAG, split);
        }

        for (int i = 2; i < 4; i++) {
            Scanner scanner = new Scanner(splits[i]);
            if (!scanner.hasNextInt()) {
                return false;
            }
        }

        return true;
    }

    private void startDownload(String url, AppCompatActivity activity) {
        Log.v(TAG, "Download file...");
        ConanCastFileController conanCastFileController = Model
                .getInstance().getConanCastFileController();
        conanCastFileController.downloadConanCastFile(activity, url);
    }

    private boolean isConanNewsLink(String url) {
        return url.contains("conannews.org");
    }

    private boolean isConanCastDownloadLink(Element linkElement) {
        return linkElement.className().equals("powerpress_link_d");
    }
}
