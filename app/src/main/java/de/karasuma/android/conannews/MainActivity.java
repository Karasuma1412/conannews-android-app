package de.karasuma.android.conannews;

import android.os.AsyncTask;
import de.karasuma.android.conannews.data.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import de.karasuma.android.conannews.communication.RequestPostsTask;

public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestPostsTask task = new RequestPostsTask();
        task.execute();

        recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initFakeData() {
        Model model = Model.getInstance();
        model.getPosts().add(new Post("Title1", "content1"));
        model.getPosts().add(new Post("Title2", "content2"));
        model.getPosts().add(new Post("Title3", "content3"));

    }

    public void updatePosts(JSONObject jsonObject) {
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
