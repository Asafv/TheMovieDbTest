package com.asafvaron.themoviedbtest.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.fragments.MoviesFragment;
import com.asafvaron.themoviedbtest.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MovieViewHolder> {
    private static final String TAG = MoviesGridAdapter.class.getSimpleName();
    private final List<Movie> mData;
    private MoviesGridAdapterListener mListener;

    public void setListener(MoviesFragment listener) {
        this.mListener = (MoviesGridAdapterListener) listener;
    }

    public interface MoviesGridAdapterListener {
        void onMovieClicked(int pos);
    }

    public MoviesGridAdapter(List<Movie> data) {
        this.mData = data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mData.get(position);
        final int pos = position;

        String title = movie.getTitle();
        if (title == null || title.isEmpty())
            title = movie.getOriginalTitle();

        holder.tv_title.setText(title);
        Glide.with(MyApp.getContext())
                .load(movie.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_upload)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.iv_poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: movie: " + movie);
                mListener.onMovieClicked(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView iv_poster;

        MovieViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_movie_title);
            iv_poster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
