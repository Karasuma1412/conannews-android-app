package de.karasuma.android.conannews.menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class ImpressumMenuAction implements MenuAction {
    private final Activity activity;

    public ImpressumMenuAction(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String impressumURL = "https://conanwiki.org/wiki/ConanWiki:Impressum";
        i.setData(Uri.parse(impressumURL));
        activity.startActivity(i);
        return true;
    }
}
