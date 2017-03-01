package com.asafvaron.themoviedbtest.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
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

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.R;
import com.asafvaron.themoviedbtest.activity.MainActivity;
import com.asafvaron.themoviedbtest.adapters.MoviesGridAdapter;
import com.asafvaron.themoviedbtest.database.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.model.MoviesResponse;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;
import com.asafvaron.themoviedbtest.views.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesGridFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MoviesGridAdapter.MoviesGridAdapterListener {

    private static final String TAG = MoviesGridFragment.class.getSimpleName();

    private static final int LOADER_ID = 1;
    private MoviesGridAdapter mMoviesGridAdapter;
    private List<Movie> mCurrentMovieList;
    private ApiInterface mApiService;
    private String mCurrentDbType;
    private SharedPreferences mPrefs;

    @BindView(R.id.progress)
    ProgressBar mProgress;

    @BindView(R.id.rv_grid_list)
    RecyclerView mRvGridList;

    public static MoviesGridFragment newInstance() {
        return new MoviesGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setHasOptionsMenu(true);
        mPrefs = MyApp.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        mApiService = ApiClient.getClient().create(ApiInterface.class);
        mCurrentDbType = mPrefs.getString("last_db_type", MoviesContract.MovieTypes.NOW_PLAYING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View root = inflater.inflate(R.layout.movies_grid_frag_layout, container, false);
        ButterKnife.bind(this, root);
        setupRecyclerView();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

        try {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } catch (Exception e) {
            Log.e(TAG, "onActivityCreated: ERR: " + e.getLocalizedMessage());
            getMoviesFromApi(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        changeActionbarTitle();
        inflater.inflate(R.menu.movie_grid_menu, menu);
        // XXX Yossi please let me know if that's the way
        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowHomeEnabled(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        Call<MoviesResponse> call;
        switch (itemId) {
            case R.id.action_now_playing:
                call = mApiService.getNowPlayingMovies(ApiClient.API_KEY);
                mCurrentDbType = MoviesContract.MovieTypes.NOW_PLAYING;
                querySearch(call);
                return true;
            case R.id.action_upcoming:
                call = mApiService.getUpcomingMovies(ApiClient.API_KEY);
                mCurrentDbType = MoviesContract.MovieTypes.UPCOMING;
                querySearch(call);
                return true;
            case R.id.action_top_rated:
                call = mApiService.getTopRatedMovies(ApiClient.API_KEY);
                mCurrentDbType = MoviesContract.MovieTypes.TOP_RATED;
                querySearch(call);
                return true;
            case R.id.action_popular:
                call = mApiService.getPopularMovies(ApiClient.API_KEY);
                mCurrentDbType = MoviesContract.MovieTypes.POPULAR;
                querySearch(call);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void querySearch(Call<MoviesResponse> call) {
        mCurrentMovieList = null;
        saveLastDbType(mCurrentDbType);
        getMoviesFromApi(call);
        changeActionbarTitle();
    }

    private void changeActionbarTitle() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mCurrentDbType);
    }

    private void saveLastDbType(String currentDbType) {
        mPrefs.edit().putString("last_db_type", currentDbType).apply();
    }

    private void setupRecyclerView() {
        mRvGridList.setHasFixedSize(true);
        mRvGridList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvGridList.setItemAnimator(new DefaultItemAnimator());
        // set itemDecoration for grid spacing
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(getContext(), R.dimen.grid_item_offset);
        mRvGridList.addItemDecoration(decoration);
    }

    /**
     * use this method to request the movies from The Movie DB api server
     * and save to data base on success
     *
     * @param call pass null to use default
     */
    private void getMoviesFromApi(Call<MoviesResponse> call) {
        // set the default
        if (call == null)
            call = mApiService.getNowPlayingMovies(ApiClient.API_KEY);

        Log.d(TAG, "getMoviesFromApi: url: " + call.request().url());

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                saveToDb(movies);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ERR: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * saves the last success query result to DB
     *
     * @param movies result returned from server api
     */
    private void saveToDb(List<Movie> movies) {
        for (Movie m : movies) {
            MyApp.getContext().getContentResolver()
                    .insert(MoviesContract.Movies.CONTENT_URI, m.getValues(mCurrentDbType));
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        mProgress.setVisibility(View.VISIBLE);

        // query the database using the movie type
        String selection = MoviesContract.Movies.COLUMN_TYPE + "=?";
        String[] selectionArgs = {mCurrentDbType};

        return new CursorLoader(getContext(), MoviesContract.Movies.CONTENT_URI,
                null, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if (mCurrentMovieList != null)
            return;

        if (c.moveToNext()) {
            Log.d(TAG, "onLoadFinished: c != null");
            mCurrentMovieList = getMovies(c);
            Log.d(TAG, "mCurrentMovieList.size(): " + mCurrentMovieList.size());

            mMoviesGridAdapter = new MoviesGridAdapter(mCurrentMovieList);
            mMoviesGridAdapter.setListener(this);
            mRvGridList.setAdapter(mMoviesGridAdapter);
            mProgress.setVisibility(View.GONE);
        } else {
            Log.w(TAG, "onLoadFinished: c == null");
            getMoviesFromApi(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    public List<Movie> getMovies(Cursor c) {
        List<Movie> movies = new ArrayList<>();

        while (c.moveToNext()) {
            Movie m = new Movie();
            m.setId(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_OVERVIEW)));
            m.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_AVERAGE)));
            m.setVoteCount(c.getInt(c.getColumnIndex(MoviesContract.Movies.COLUMN_VOTE_COUNT)));
            m.setPosterPath(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MoviesContract.Movies.COLUMN_RELEASE_DATE)));
//            Log.d(TAG, "getMovies: m= " + m.toString());
            movies.add(m);
        }
        return movies;
    }

    @Override
    public void onMovieClicked(int pos) {
        Movie movie = mCurrentMovieList.get(pos);
        ((MainActivity) getActivity()).showInfoFragment(movie);
    }
}
