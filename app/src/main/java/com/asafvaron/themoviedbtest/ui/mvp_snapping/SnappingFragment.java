package com.asafvaron.themoviedbtest.ui.mvp_snapping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.data.api.MoviesApi;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.ui.movie_details.MovieDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asafvaron on 01/03/2017.
 */
public class SnappingFragment extends Fragment
        implements SnapAdapter.SnapAdapterListener, SnappingContract.View {
    private static final String TAG = SnappingFragment.class.getSimpleName();

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_now_playing_list)
    RecyclerView mRvNowPlayingList;

    @BindView(R.id.rv_top_rated_list)
    RecyclerView mRvTopRatedList;

    @BindView(R.id.rv_upcoming_list)
    RecyclerView mRvUpcomingList;

    @BindView(R.id.rv_popular_list)
    RecyclerView mRvPopularList;

    // commented out in xml
//    @BindView(R.id.rv_snap_vertical_list)
//    RecyclerView mRvSnapVerticalList;
//    private List<Movie> movieVerticalList;

    private List<Movie> movieHorizontalList;
    private SnappingContract.Presenter mActionListener;


    public static SnappingFragment newInstance() {
        return new SnappingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActionListener = new SnappingPresenter(MoviesApi.getInstance().getMoviesService(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.snapping_frag_layout, container, false);
        ButterKnife.bind(this, root);

        setupRecyclerViews();

//        setupVerticalList();

        return root;
    }

    private void setupVerticalList() {
        //        mRvSnapVerticalList.setLayoutManager(new LinearLayoutManager(getContext(),
//                LinearLayoutManager.VERTICAL, false));
//        snapHelper.attachToRecyclerView(mRvSnapVerticalList);

        // get vertical list
//        mApiService.getTopRatedMovies(ApiClient.API_KEY)
//                .enqueue(new Callback<MoviesResponse>() {
//                    @Override
//                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
//                        movieVerticalList = response.body().getResults();
//                        Log.d(TAG, "onResponse: movies size: " + movieVerticalList.size());
//                        SnapAdapter snapAdapter = new SnapAdapter(movieVerticalList, false);
////                        mRvSnapVerticalList.setAdapter(snapAdapter);
//                        snapAdapter.setListener(SnappingFragment.this);
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
//                    }
//                });
    }

    private void setupRecyclerViews() {
        // create the gravity snap helper
//        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper = new LinearSnapHelper();

        // attach to recyclerViews
        snapHelper.attachToRecyclerView(mRvNowPlayingList);
        snapHelper.attachToRecyclerView(mRvTopRatedList);
        snapHelper.attachToRecyclerView(mRvPopularList);
        snapHelper.attachToRecyclerView(mRvUpcomingList);

        // attach the layout managers
        mRvNowPlayingList.setLayoutManager( new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRvTopRatedList.setLayoutManager( new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRvPopularList.setLayoutManager( new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRvUpcomingList.setLayoutManager( new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Movies Snap List");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionListener.loadData(false);
    }

    @Override
    public void onNowPlayingLoaded(List<? extends Movie> movies) {
        SnapAdapter snapAdapter = new SnapAdapter((List<Movie>) movies, true);
        mRvNowPlayingList.setAdapter(snapAdapter);
        snapAdapter.setListener(this);
    }

    @Override
    public void onTopRatedLoaded(List<? extends Movie> movies) {
        SnapAdapter snapAdapter = new SnapAdapter((List<Movie>) movies, true);
        mRvTopRatedList.setAdapter(snapAdapter);
        snapAdapter.setListener(this);
    }

    @Override
    public void onUpcomingLoaded(List<? extends Movie> movies) {
        SnapAdapter snapAdapter = new SnapAdapter((List<Movie>) movies, true);
        mRvUpcomingList.setAdapter(snapAdapter);
        snapAdapter.setListener(this);
    }

    @Override
    public void onPopularLoaded(List<? extends Movie> movies) {
        SnapAdapter snapAdapter = new SnapAdapter((List<Movie>) movies, true);
        mRvPopularList.setAdapter(snapAdapter);
        snapAdapter.setListener(this);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frags_container,
                        MovieDetailFragment.newInstance(movie), MovieDetailFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }

}
