package com.asafvaron.themoviedbtest.movie_snapping;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.fragments.MovieInfoFragment;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.model.MoviesResponse;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asafvaron on 01/03/2017.
 */
public class SnappingFragment extends Fragment implements SnapAdapter.SnapAdapterListener {
    private static final String TAG = SnappingFragment.class.getSimpleName();

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_snap_horizontal_list)
    RecyclerView mRvSnapHorizontalList;

    @BindView(R.id.rv_snap_vertical_list)
    RecyclerView mRvSnapVerticalList;

    private ApiInterface mApiService;

    private List<Movie> movieHorizontalList;
    private List<Movie> movieVerticalList;


    public static SnappingFragment newInstance() {
        return new SnappingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mApiService = ApiClient.getClient().create(ApiInterface.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.snapping_frag_layout, container, false);
        ButterKnife.bind(this, root);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRvSnapHorizontalList);
        mRvSnapHorizontalList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        mRvSnapVerticalList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        snapHelper.attachToRecyclerView(mRvSnapVerticalList);

        // get horizontal list
        mApiService.getNowPlayingMovies(ApiClient.API_KEY)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        movieHorizontalList = response.body().getResults();
                        Log.d(TAG, "onResponse: movies size: " + movieHorizontalList.size());
                        SnapAdapter snapAdapter = new SnapAdapter(movieHorizontalList, true);
                        mRvSnapHorizontalList.setAdapter(snapAdapter);
                        snapAdapter.setListener(SnappingFragment.this);

                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });

        // get vertical list
        mApiService.getTopRatedMovies(ApiClient.API_KEY)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        movieVerticalList = response.body().getResults();
                        Log.d(TAG, "onResponse: movies size: " + movieVerticalList.size());
                        SnapAdapter snapAdapter = new SnapAdapter(movieVerticalList, false);
                        mRvSnapVerticalList.setAdapter(snapAdapter);
                        snapAdapter.setListener(SnappingFragment.this);

                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MovieSnapActivity) getActivity()).getSupportActionBar().setTitle("Movies Snap List");
        ((MovieSnapActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onMovieClicked(int pos) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frags_container,
                        MovieInfoFragment.newInstance(movieHorizontalList.get(pos)), MovieInfoFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}
