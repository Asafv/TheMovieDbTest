package com.asafvaron.themoviedbtest.movie_snapping;

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
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by asafvaron on 01/03/2017.
 */
public class SnapAdapter extends RecyclerView.Adapter<SnapAdapter.ViewHolder> {
    private static final String TAG = SnapAdapter.class.getSimpleName();

    private final List<Movie> mMoviesList;
    private final boolean horizontal;
    private SnapAdapterListener mListener;

    public interface SnapAdapterListener {
        void onMovieClicked(int pos);
    }

    public void setListener(SnappingFragment listener) {
        this.mListener = listener;
    }

    public SnapAdapter(List<Movie> movies, boolean horizontal) {
        this.mMoviesList = movies;
        this.horizontal = horizontal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (horizontal) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.snap_movie_horizontal_item, parent, false)
            );
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.snap_movie_vertical_item, parent, false)
            );
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final Movie movie = mMoviesList.get(position);
        final int pos = position;
        Log.d(TAG, "movie: " + movie.getTitle());

        String title = movie.getTitle();
        if (title == null || title.isEmpty())
            title = movie.getOriginalTitle();

        holder.tv_snap_title.setText(title);

        Glide.with(MyApp.getContext())
                .load(ApiClient.IMAGE_URL + movie.getPosterPath())
                .placeholder(android.R.drawable.ic_menu_upload)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.iv_snap_poster);


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
        return mMoviesList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_snap_poster;
        TextView tv_snap_title;

        ViewHolder(View itemView) {
            super(itemView);
            iv_snap_poster = (ImageView) itemView.findViewById(R.id.iv_snap_poster);
            tv_snap_title = (TextView) itemView.findViewById(R.id.tv_snap_title);
        }
    }
}
