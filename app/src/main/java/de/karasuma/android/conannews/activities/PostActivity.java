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
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.ImpressumDataProtectionMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;

public class PostActivity extends AppCompatActivity {

    private View progressCircle;
    private HashMap<Integer, MenuAction> menuActionMap;

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

        menuActionMap = new HashMap<>();
        menuActionMap.put(R.id.home_menu_item, new HomeMenuAction(this));
        menuActionMap.put(R.id.new_conancast_menu_item, new ConanCastMenuAction(this));
        menuActionMap.put(R.id.conancast_downloaded_menu_item, new ConanCastDownloadedMenuAction(this));
        menuActionMap.put(R.id.category_anime_de_menu_item, new CategoryFilterMenuAction(this, "anime-de"));
        menuActionMap.put(R.id.category_anime_jp_menu_item, new CategoryFilterMenuAction(this, "anime-jp"));
        menuActionMap.put(R.id.category_manga_de_menu_item, new CategoryFilterMenuAction(this, "manga-de"));
        menuActionMap.put(R.id.category_manga_jp_menu_item, new CategoryFilterMenuAction(this, "manga-jp"));
        menuActionMap.put(R.id.impressum_data_protection_menu_item, new ImpressumDataProtectionMenuAction(this));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuAction menuAction = menuActionMap.get(item.getItemId());

        if (menuAction == null) {
            Log.e("MainActivity", "invalid menu action");
            return false;
        }

        return menuAction.execute();
    }

    public View getProgressCircle() {
        return progressCircle;
    }
}
