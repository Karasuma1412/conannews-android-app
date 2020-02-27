package de.karasuma.android.conannews.data;

import java.util.ArrayList;

import de.karasuma.android.conannews.filehandling.ConanCastFileController;
import de.karasuma.android.conannews.util.URLUtil;

public class Model {
    private static final Model ourInstance = new Model();
    private ArrayList<Post> posts = new ArrayList<>();
    private ConanCastFileController conanCastFileController;
    private URLUtil urlUtil;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ConanCastFileController getConanCastFileController() {
        if (conanCastFileController == null) {
            conanCastFileController = new ConanCastFileController();
        }
        return conanCastFileController;
    }

    public void setConanCastFileController(ConanCastFileController conanCastFileController) {
        this.conanCastFileController = conanCastFileController;
    }

    public URLUtil getUrlUtil() {
        if (urlUtil == null) {
            urlUtil = new URLUtil();
        }
        return urlUtil;
    }

    public void setUrlUtil(URLUtil urlUtil) {
        this.urlUtil = urlUtil;
    }
}
