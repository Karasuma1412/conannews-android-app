package de.karasuma.android.conannews.menu;

import android.app.Activity;
import android.content.Intent;

import de.karasuma.android.conannews.activities.ConanCastActivity;

public class ConanCastDownloadedMenuAction implements MenuAction {
    private final Activity activity;
    private final String TAG = "ConanCastDownloadedMA";

    public ConanCastDownloadedMenuAction(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        String url = "https://conannews.org/category/conancast/";
        Intent intent = new Intent(activity, ConanCastActivity.class);
        intent.putExtra("filterURL", url);
        activity.startActivity(intent);
        return true;
    }
}
