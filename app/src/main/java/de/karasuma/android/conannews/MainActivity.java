package de.karasuma.android.conannews;

import de.karasuma.android.conannews.communication.html.RequestPostsTask;
import de.karasuma.android.conannews.data.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private View progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressCircular = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RequestPostsTask task = new RequestPostsTask(this);
        task.execute();

        recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts(), this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initFakeData() {
        Model model = Model.getInstance();
    }

    public void updatePosts() {
        //recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts());
        //recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public View getProgressCircular() {
        return progressCircular;
    }
}
