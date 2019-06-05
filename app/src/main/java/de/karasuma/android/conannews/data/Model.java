package de.karasuma.android.conannews.data;

import java.util.ArrayList;

public class Model {
    private static final Model ourInstance = new Model();
    private ArrayList<Post> posts = new ArrayList<>();

    public static Model getInstance() {
        return ourInstance;
    }
    private Model() {

    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
