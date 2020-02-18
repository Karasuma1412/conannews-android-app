package de.karasuma.android.conannews.menu;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import de.karasuma.android.conannews.activities.MainActivity;

public class CategoryFilterMenuAction implements MenuAction {
    private final String tag = "CategoryFilterMenu";
    private final Activity activity;
    private final String category;
    private String categoryFilterURL = "https://conannews.org/category/";


    public CategoryFilterMenuAction(Activity activity, String category) {
        this.activity = activity;
        this.category = category;
    }

    @Override
    public boolean execute() {
        Log.v(tag, "clicked CategoryFilterMenuItem");
        Log.v(tag, "ConanCastMenuItem clicked");
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("filterURL", categoryFilterURL + category + "/");
        activity.startActivity(intent);
        return true;
    }
}
