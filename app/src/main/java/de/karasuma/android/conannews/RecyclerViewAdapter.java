package de.karasuma.android.conannews;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import de.karasuma.android.conannews.data.Category;
import de.karasuma.android.conannews.data.Post;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Post> posts;
    private final MainActivity mainActivity;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public RecyclerViewAdapter(List<Post> posts, MainActivity mainActivity) {
        this.posts = posts;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item
                    , viewGroup, false);
            PostsViewHolder viewHolder = new PostsViewHolder(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_item
                    , viewGroup, false);
            LoadingViewHolder viewHolder = new LoadingViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        if (viewHolder instanceof PostsViewHolder) {
            populatePostsRows((PostsViewHolder) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingViewHolder((LoadingViewHolder) viewHolder, i);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingViewHolder(LoadingViewHolder viewHolder, int i) {

    }

    private void populatePostsRows(PostsViewHolder postsViewHolder, final int i) {
        postsViewHolder.title.setText(posts.get(i).getTitle());
        postsViewHolder.cover.setImageBitmap(posts.get(i).getBitmap());
        postsViewHolder.summary.setText(posts.get(i).getSummary());

        //author and date
        postsViewHolder.postInfo.removeAllViews();
        LinearLayout postInfo = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.article_info, postsViewHolder.postInfo, false);
        TextView publishedView = (TextView) postInfo.getChildAt(0);
        publishedView.setText(posts.get(i).getPublished());

        TextView authorView = (TextView) postInfo.getChildAt(1);
        authorView.setText(posts.get(i).getAuthor());

        postsViewHolder.postInfo.addView(postInfo);
        //author and date end

        postsViewHolder.categories.removeAllViews();

        for (final Category category : posts.get(i).getCategories()) {
            CardView cardView;
            cardView = (CardView) mainActivity.getLayoutInflater().inflate(R.layout.category_item, postsViewHolder.cardView, false);
            TextView categoryText = (TextView) cardView.getChildAt(0);
            categoryText.setText(category.getName());
            categoryText.setBackgroundColor(Color.parseColor(category.getColor()));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mainActivity, MainActivity.class);
                    intent.putExtra("filterURL", category.getFilterURL());
                    mainActivity.startActivity(intent);
                }
            });
            postsViewHolder.categories.addView(cardView);
        }
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
        LinearLayout postInfo;
        LinearLayout categories;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.title);
            cover = itemView.findViewById(R.id.cover);
            summary = itemView.findViewById(R.id.summary);
            postInfo = itemView.findViewById(R.id.post_info);
            categories = itemView.findViewById(R.id.categories);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


}
