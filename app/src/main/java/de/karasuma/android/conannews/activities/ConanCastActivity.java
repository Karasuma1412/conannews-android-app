package de.karasuma.android.conannews.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import de.karasuma.android.conannews.ConanCastAdapter;
import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.filehandling.ConanCastFileController;

public class ConanCastActivity extends Activity {

    private final String TAG = "ConanCastActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conancast);

        ConanCastFileController conanCastFileController = Model.getInstance().getConanCastFileController();
        File[] files = conanCastFileController.getConanCastFiles(this);

        ArrayList<File> fileList = new ArrayList<>();

        for (File file : files) {
            Log.v(TAG, file.toString());
            fileList.add(file);
        }

        ConanCastAdapter adapter = new ConanCastAdapter(this, fileList);
        Log.v(TAG, adapter.toString());
        ListView listView = findViewById(R.id.list_view_conanncast);
        Log.v(TAG, listView.toString());
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty_conancast));
    }
}
