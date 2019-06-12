package de.karasuma.android.conannews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import de.karasuma.android.conannews.communication.html.OpenPostTask;

public class PostActivity extends AppCompatActivity {

    private View progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        progressCircle = findViewById(R.id.progress_circular);
        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("url");
        OpenPostTask openPostTask = new OpenPostTask(this);
        openPostTask.execute(url);
    }

    public View getProgressCircle() {
        return progressCircle;
    }
}
