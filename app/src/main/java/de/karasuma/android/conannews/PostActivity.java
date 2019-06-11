package de.karasuma.android.conannews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Bundle bundle = getIntent().getExtras();

        String content = bundle.getString("content");
        TextView contentTextView = findViewById(R.id.content);
        contentTextView.setText(content);

        String title = bundle.getString("title");
        TextView titleTextView = findViewById(R.id.title);
        titleTextView.setText(title);
    }
}
