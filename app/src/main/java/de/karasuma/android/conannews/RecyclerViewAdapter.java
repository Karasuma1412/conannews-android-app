package de.karasuma.android.conannews;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.karasuma.android.conannews.data.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PostsViewHolder> {

    private final List<Post> posts;

    public RecyclerViewAdapter (List<Post> posts) {
        this.posts = posts;
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
    public void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, int i) {
        postsViewHolder.title.setText(posts.get(i).getTitle());
        postsViewHolder.cover.setImageBitmap(posts.get(i).getBitmap());
        postsViewHolder.published.setText(posts.get(i).getDate());
        postsViewHolder.summary.setText(posts.get(i).getSummary());
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

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.title);
            cover = itemView.findViewById(R.id.cover);
            summary = itemView.findViewById(R.id.summary);
            published = itemView.findViewById(R.id.published);
        }
    }


}
