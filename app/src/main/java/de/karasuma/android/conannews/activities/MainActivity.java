package de.karasuma.android.conannews.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.karasuma.android.conannews.R;
import de.karasuma.android.conannews.RecyclerViewAdapter;
import de.karasuma.android.conannews.communication.html.RequestPostsTask;
import de.karasuma.android.conannews.data.Model;
import de.karasuma.android.conannews.data.Post;
import de.karasuma.android.conannews.menu.CategoryFilterMenuAction;
import de.karasuma.android.conannews.menu.ConanCastDownloadedMenuAction;
import de.karasuma.android.conannews.menu.ConanCastMenuAction;
import de.karasuma.android.conannews.menu.DataProtectionMenuAction;
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.ImpressumMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;
import de.karasuma.android.conannews.menu.MenuController;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private View progressCircular;
    boolean isLoading = false;
    ArrayList<Post> posts = Model.getInstance().getPosts();
    MainActivity mainActivity;
    int pageIndex = 1;
    String conanNewsURL = "https://conannews.org/";
    String pageURL = "page/";
    private MenuController menuController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
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

        recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts(), this);
        recyclerView.setAdapter(recyclerViewAdapter);
        initScrollListener();

        loadNextPosts();

        FirebaseMessaging.getInstance().subscribeToTopic("conannews")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "message";
                        if (!task.isSuccessful()) {
                            msg = "msg not successful";
                        }
                        Log.d(TAG, msg);
                    }
                });


    }

    private void loadNextPosts() {
        RequestPostsTask task = new RequestPostsTask(this);
        Log.v(TAG, conanNewsURL);
        if (conanNewsURL.charAt(conanNewsURL.length() - 1) != '/') {
            conanNewsURL += '/';
        }

        if (pageIndex == 1) {
            task.execute(conanNewsURL);
        } else {
            task.execute(conanNewsURL + pageURL + pageIndex + '/');
        }
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
        menuController = new MenuController(this);
        menuController.initialize();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuController.execute(item.getItemId());
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("conannews", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setConanNewsURL(String conanNewsURL) {
        this.conanNewsURL = conanNewsURL;
        int pageIndex = this.conanNewsURL.indexOf("page");

        if (pageIndex == -1) {
            return;
        }

        this.conanNewsURL = conanNewsURL.substring(0, pageIndex);
    }

    public View getProgressCircular() {
        return progressCircular;
    }
}
