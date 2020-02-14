package de.karasuma.android.conannews.menu;

import android.app.Activity;
import android.util.Log;

public class CategoryFilterMenuAction implements MenuAction {
    private final String tag = "CategoryFilterMenu";
    private final Activity activity;


    public CategoryFilterMenuAction(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean execute() {
        Log.v(tag, "clicked CategoryFilterMenuItem");
        return false;
    }
}
