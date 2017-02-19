package com.asafvaron.themoviedbtest.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
    private final RecyclerView mRvGridList;
    private final List<MyMovie> mData;
    private final LayoutInflater mInflater;

    public MoviesGridAdapter(Context context, RecyclerView rv, List<MyMovie> data) {
        this.mContext = context;
        this.mRvGridList = rv;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
        init();
    }

    private void init() {
        mRvGridList.setHasFixedSize(true);
        mRvGridList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRvGridList.setItemAnimator(new DefaultItemAnimator());
        // set itemDecoration for grid spacing
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(mContext, R.dimen.grid_item_offset);
        mRvGridList.addItemDecoration(decoration);
        mRvGridList.setAdapter(this);
        this.notifyDataSetChanged();
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
                .load(myMovie.getPoster())
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

    /* Grid Item Spacing Decoration */
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        GridSpacingItemDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        GridSpacingItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
