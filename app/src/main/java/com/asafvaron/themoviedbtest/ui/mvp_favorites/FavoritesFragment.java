package com.asafvaron.themoviedbtest.ui.mvp_favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.decorations.GridSpacingItemDecoration;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 05/03/2017.
 */
public class FavoritesFragment extends Fragment
        implements FavoritesContract.View, FavoritesAdapter.ClickListener {

    private static final String TAG = "FavoritesFragment";

    @BindView(R.id.tv_info)
    TextView mTvInfo;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_favs_list)
    RecyclerView mRvFavsList;

    private FavoritesContract.UserActions mActionListener;
    private FavoritesAdapter mFavsAdapter;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionListener = new FavoritesPresenter(getLoaderManager(), this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favorites_frag_layout, container, false);
        ButterKnife.bind(this, root);

        // set recyclerView adapter
        mFavsAdapter = new FavoritesAdapter(new ArrayList<>(0), this);
        setupRecyclerView();
        mRvFavsList.setAdapter(mFavsAdapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        changeActionbarTitle();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void changeActionbarTitle() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.favorites_title));
    }

    private void setupRecyclerView() {
        mRvFavsList.setHasFixedSize(true);
        mRvFavsList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvFavsList.setItemAnimator(new DefaultItemAnimator());
        // set itemDecoration for grid spacing
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(getContext(), R.dimen.grid_item_offset);
        mRvFavsList.addItemDecoration(decoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionListener.loadFavorites();
    }

    @Override
    public void onLoadComplete(List<Movie> favsList) {
        Log.d(TAG, "onLoadComplete: favsList: " + favsList);
        if (favsList.size() == 0) {
            mTvInfo.setText(getString(R.string.no_favorites_found));
        } else {
            mTvInfo.setVisibility(View.GONE);
            mFavsAdapter.updateData(favsList);
        }
    }

    @Override
    public void updateProgress(boolean isUpdating) {
        Log.d(TAG, "updateProgress() returned: " + isUpdating);
        mProgressBar.setVisibility((isUpdating) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMovieClicked(Movie m) {
        Log.d(TAG, "onMovieClicked: " + m.getTitle());
//        ((MainActivity) getActivity()).showInfoFragment(m);
    }
}
