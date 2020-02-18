package de.karasuma.android.conannews.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.karasuma.android.conannews.ConanCastAdapter;
import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.filehandling.ConanCastFileController;
import de.karasuma.android.conannews.menu.CategoryFilterMenuAction;
import de.karasuma.android.conannews.menu.ConanCastDownloadedMenuAction;
import de.karasuma.android.conannews.menu.ConanCastMenuAction;
import de.karasuma.android.conannews.menu.DataProtectionMenuAction;
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.ImpressumMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;

public class ConanCastActivity extends AppCompatActivity {

    private final String TAG = "ConanCastActivity";
    private HashMap<Integer, MenuAction> menuActionMap;

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
        menuActionMap.put(R.id.impressum_menu_item, new ImpressumMenuAction(this));
        menuActionMap.put(R.id.data_protection_menu_item, new DataProtectionMenuAction(this));
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
}
