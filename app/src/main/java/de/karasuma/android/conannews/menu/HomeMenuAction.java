package de.karasuma.android.conannews.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.karasuma.android.conannews.MainActivity;

public class HomeMenuAction implements MenuAction {

    private final AppCompatActivity activity;

    public HomeMenuAction(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        Log.v("MainActivity", "clicked home_menu_item");
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        return true;
    }
}
