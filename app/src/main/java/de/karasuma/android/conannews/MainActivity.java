package de.karasuma.android.conannews;

import de.karasuma.android.conannews.communication.html.RequestPostsTask;
import de.karasuma.android.conannews.data.*;
import de.karasuma.android.conannews.menu.CategoryFilterMenuAction;
import de.karasuma.android.conannews.menu.ConanCastMenuAction;
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private View progressCircular;
    boolean isLoading = false;
    ArrayList<Post> posts = Model.getInstance().getPosts();
    MainActivity mainActivity;
    int pageIndex = 1;
    String conanNewsURL = "https://conannews.org/";
    String pageURL = "page/";

    private HashMap<Integer, MenuAction> menuActionMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts.clear();
        setContentView(R.layout.activity_main);
        mainActivity = this;

        progressCircular = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("filterURL")) {
            conanNewsURL = bundle.getString("filterURL");
        }

        loadNextPosts();


        recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts(), this);
        recyclerView.setAdapter(recyclerViewAdapter);
        initScrollListener();
    }

    private void loadNextPosts() {
        RequestPostsTask task = new RequestPostsTask(this);
        task.execute(conanNewsURL + pageURL + pageIndex + "/");
        pageIndex++;
    }

    private void initScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ArrayList<Post> posts = Model.getInstance().getPosts();

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == posts.size() - 1) {
                        loadMore();
                    }
                }
            }

        });

    }

    private void loadMore() {
        isLoading = true;
        System.out.println("loading more content...");
        posts = Model.getInstance().getPosts();
        posts.add(null);

        recyclerView.post(new Runnable() {
            public void run() {
                recyclerViewAdapter.notifyItemInserted(posts.size() - 1);
            }
        });

        loadNextPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        menuActionMap = new HashMap<>();
        menuActionMap.put(R.id.home_menu_item, new HomeMenuAction(this));
        menuActionMap.put(R.id.conancast_menu_item, new ConanCastMenuAction(this));
        menuActionMap.put(R.id.category_filter_menu_item, new CategoryFilterMenuAction(this));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuAction menuAction = menuActionMap.get(item.getItemId());

        if (menuAction == null) {
            Log.e("MainActivity", "invalid menu action");
            return false;
        }

        return menuAction.execute();
    }

    private void initFakeData() {
        Model model = Model.getInstance();
    }

    public void updatePosts() {
        //recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts());
        //recyclerView.setAdapter(recyclerViewAdapter);
        isLoading = false;

        Iterator<Post> iterator = posts.iterator();

        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public View getProgressCircular() {
        return progressCircular;
    }
}
