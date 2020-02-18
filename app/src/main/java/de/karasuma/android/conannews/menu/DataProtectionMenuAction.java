package de.karasuma.android.conannews.menu;

import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class DataProtectionMenuAction implements MenuAction {
    private final AppCompatActivity activity;

    public DataProtectionMenuAction(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String impressumURL = "https://conanwiki.org/wiki/ConanWiki:Datenschutz";
        i.setData(Uri.parse(impressumURL));
        activity.startActivity(i);
        return true;
    }
}
