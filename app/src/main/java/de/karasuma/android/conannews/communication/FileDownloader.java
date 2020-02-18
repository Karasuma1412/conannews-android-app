package de.karasuma.android.conannews.communication;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import java.io.File;

import de.karasuma.android.conannews.R;

import static android.content.Context.DOWNLOAD_SERVICE;

public class FileDownloader {
    private long downloadID;

    public void download(Context context, String url, String path) {
        int fileNameDelimiterIndex = url.lastIndexOf("/");
        String fileName = url.substring(fileNameDelimiterIndex + 1);

        File file = new File(context.getExternalFilesDir(null), path + "/" + fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setTitle(context.getString(R.string.conannews))// Title of the Download Notification
                .setDescription(fileName)// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }
}
