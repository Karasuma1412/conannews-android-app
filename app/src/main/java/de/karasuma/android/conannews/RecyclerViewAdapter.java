package de.karasuma.android.conannews;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.karasuma.android.conannews.communication.html.OpenPostTask;
import de.karasuma.android.conannews.data.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PostsViewHolder> {

    private final List<Post> posts;
    private final MainActivity mainActivity;

    public RecyclerViewAdapter(List<Post> posts, MainActivity mainActivity) {
        this.posts = posts;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item
                , viewGroup, false);
        PostsViewHolder viewHolder = new PostsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, final int i) {
        postsViewHolder.title.setText(posts.get(i).getTitle());
        postsViewHolder.cover.setImageBitmap(posts.get(i).getBitmap());
        postsViewHolder.published.setText(posts.get(i).getPublished());
        postsViewHolder.summary.setText(posts.get(i).getSummary());
        postsViewHolder.author.setText(posts.get(i).getAuthor());
        postsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, PostActivity.class);
                intent.putExtra("url", posts.get(i).getUrl());
                mainActivity.startActivity(intent);
//                OpenPostTask task = new OpenPostTask(posts.get(i), mainActivity);
//                task.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        ImageView cover;
        TextView summary;
        TextView published;
        TextView author;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.title);
            cover = itemView.findViewById(R.id.cover);
            summary = itemView.findViewById(R.id.summary);
            published = itemView.findViewById(R.id.published);
            author = itemView.findViewById(R.id.author);
        }
    }


}
