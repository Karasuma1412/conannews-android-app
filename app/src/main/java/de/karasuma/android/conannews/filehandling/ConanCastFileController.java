package de.karasuma.android.conannews.filehandling;

import android.content.Context;
import android.util.Log;

import java.io.File;

import de.karasuma.android.conannews.activities.PostActivity;
import de.karasuma.android.conannews.communication.FileDownloader;

public class ConanCastFileController {

    private String path = "/conancast";

    public File[] getConanCastFiles(Context activity) {
        String path = activity.getExternalFilesDir(null) + this.path;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files == null) {
            return new File[0];
        }
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
        }
        return files;
    }

    public void downloadConanCastFile(PostActivity postActivity, String url) {
        FileDownloader fileDownloader = new FileDownloader();
        fileDownloader.download(postActivity, url, path);
    }

}
