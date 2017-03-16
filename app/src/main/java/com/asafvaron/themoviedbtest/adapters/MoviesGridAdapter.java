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
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.mvp_grid.GridContract;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MovieViewHolder> {

    private static final String TAG = MoviesGridAdapter.class.getSimpleName();

    private List<Movie> mData;
    private final GridContract.View mListener;

    /**
     * receives a List of Movie and the View that presents the data
     *
     * @param data
     * @param mView
     */
    public MoviesGridAdapter(List<Movie> data, GridContract.View mView) {
        this.mData = data;
        mListener = mView;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = mData.get(position);

        // set the title
        String title = movie.getTitle();
        if (title == null || title.isEmpty()) {
            title = movie.getOriginalTitle();
        }
        holder.tv_title.setText(title);

        // set the image
        Glide.with(MyApp.getContext())
                .load(ApiClient.IMAGE_URL + movie.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_upload)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.iv_poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: movie: " + movie);
                mListener.onMovieClicked(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Movie> movies) {
        // update only if there is a change in data
        if (mData != movies) {
            mData = movies;
            notifyDataSetChanged();
        }
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
