package de.karasuma.android.conannews.menu;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import de.karasuma.android.conannews.MainActivity;
import de.karasuma.android.conannews.PostActivity;

public class ConanCastMenuAction implements MenuAction {
    private final Activity activity;
    private final String tag = "ConanCastMenuAction";

    public ConanCastMenuAction(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        Log.v(tag, "ConanCastMenuItem clicked");
        String url = "https://conannews.org/category/conancast/";
        Intent intent = new Intent(activity, PostActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
        return false;
    }
}
