package de.karasuma.android.conannews.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.communication.html.OpenPostTask;
import de.karasuma.android.conannews.menu.CategoryFilterMenuAction;
import de.karasuma.android.conannews.menu.ConanCastDownloadedMenuAction;
import de.karasuma.android.conannews.menu.ConanCastMenuAction;
import de.karasuma.android.conannews.menu.DataProtectionMenuAction;
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.ImpressumMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;
import de.karasuma.android.conannews.menu.MenuController;

public class PostActivity extends AppCompatActivity {

    private View progressCircle;
    private MenuController menuController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        progressCircle = findViewById(R.id.progress_circular);
        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("url");
        OpenPostTask openPostTask = new OpenPostTask(this);
        openPostTask.execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menuController = new MenuController(this);
        menuController.initialize();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuController.execute(item.getItemId());
    }

    public View getProgressCircle() {
        return progressCircle;
    }
}
