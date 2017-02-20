package com.asafvaron.themoviedbtest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.objects.MyMovie;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MoviesGridAdapter extends RecyclerView.Adapter<MoviesGridAdapter.MovieViewHolder> {
    private static final String TAG = MoviesGridAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<MyMovie> mData;
    private final LayoutInflater mInflater;

    public MoviesGridAdapter(Context context, List<MyMovie> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(mInflater.inflate(R.layout.movie_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MyMovie myMovie = mData.get(position);
        Log.d(TAG, "onBindViewHolder() returned: " + myMovie.toString());

        String title = myMovie.getTitle();
        if (title == null || title.isEmpty())
            title = myMovie.getOriginal_title();

        holder.tv_title.setText(title);
        Glide.with(MyApp.getContext())
                .load(myMovie.getPoster_path())
                .placeholder(android.R.drawable.ic_menu_upload)
                .into(holder.iv_poster);
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
