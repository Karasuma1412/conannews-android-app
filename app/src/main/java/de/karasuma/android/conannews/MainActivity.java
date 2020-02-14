package de.karasuma.android.conannews;

import de.karasuma.android.conannews.communication.html.RequestPostsTask;
import de.karasuma.android.conannews.data.*;
import de.karasuma.android.conannews.menu.CategoryFilterMenuAction;
import de.karasuma.android.conannews.menu.ConanCastMenuAction;
import de.karasuma.android.conannews.menu.HomeMenuAction;
import de.karasuma.android.conannews.menu.MenuAction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


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

    private HashMap<Integer, MenuAction> menuActionMap;

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

        loadNextPosts();


        recyclerViewAdapter = new RecyclerViewAdapter(Model.getInstance().getPosts(), this);
        recyclerView.setAdapter(recyclerViewAdapter);
        initScrollListener();

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
        menuActionMap.put(R.id.category_anime_de_menu_item, new CategoryFilterMenuAction(this, "anime-de"));
        menuActionMap.put(R.id.category_anime_jp_menu_item, new CategoryFilterMenuAction(this, "anime-jp"));
        menuActionMap.put(R.id.category_manga_de_menu_item, new CategoryFilterMenuAction(this, "manga-de"));
        menuActionMap.put(R.id.category_manga_jp_menu_item, new CategoryFilterMenuAction(this, "manga-jp"));

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


    public View getProgressCircular() {
        return progressCircular;
    }
}
