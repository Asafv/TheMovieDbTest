package com.asafvaron.themoviedbtest.ui.mvp_favorites;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 05/03/2017.
 */
class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private static final String TAG = "FavoritesAdapter";

    private List<Movie> mMovies;
    private final ClickListener mListener;

    void updateData(List<Movie> favsList) {
        mMovies = favsList;
        this.notifyDataSetChanged();
    }

    interface ClickListener {
        void onMovieClicked(Movie m);
    }

    FavoritesAdapter(List<Movie> movies, FavoritesFragment listener) {
        this.mMovies = movies;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie m = mMovies.get(position);
        // set title
//        holder.tv_title.setText(m.getTitle());

        // set poster image
        Log.d(TAG, "onBindViewHolder: poster url: " + m.getPosterPath());
        Glide.with(MyApp.getContext())
                .load(m.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_upload)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.iv_poster);

        holder.itemView.setOnClickListener(v -> mListener.onMovieClicked(m));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_poster)
        ImageView iv_poster;

//        @BindView(R.id.tv_favs_movie_title)
//        TextView tv_title;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
