package com.asafvaron.themoviedbtest.ui.mvp_snapping;


import android.support.annotation.NonNull;
import android.util.Log;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.data.api.MoviesService;
import com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract;
import com.asafvaron.themoviedbtest.model.database.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.model.MoviesResponse;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 05/03/2017.
 */
public class SnappingPresenter implements SnappingContract.UserActions {
    private static final String TAG = SnappingPresenter.class.getSimpleName();

    private final MoviesService mApiService;
    private final SnappingContract.View mSnappingView;

    SnappingPresenter(@NonNull MoviesService apiService, @NonNull SnappingContract.View snappingView) {
        this.mApiService = checkNotNull(apiService, "ApiInterface cannot be null!");
        this.mSnappingView = checkNotNull(snappingView, "SnappingContract.View cannot be null!");
    }

    @Override
    public void loadData(boolean forceLoad) {
        getNowPlaying();
        getTopRated();
        getUpcoming();
        getPopular();
    }

    private void getPopular() {
        Log.d(TAG, "getPopular: ");
        mApiService.getPopularMovies()
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        if (movies != null) {
                            Log.d(TAG, "onResponse: loading popular list");
                            mSnappingView.onPopularLoaded(movies);
                            saveToDb(movies, MoviesContract.MovieTypes.POPULAR);
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });
    }

    private void getUpcoming() {
        Log.d(TAG, "getUpcoming: ");
        mApiService.getUpcomingMovies()
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        if (movies != null) {
                            Log.d(TAG, "onResponse: loading upcoming list");
                            mSnappingView.onUpcomingLoaded(movies);
                            saveToDb(movies, MoviesDbContract.MovieTypes.UPCOMING);
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });
    }

    private void getTopRated() {
        Log.d(TAG, "getTopRated: ");
        mApiService.getTopRatedMovies()
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        if (movies != null) {
                            Log.d(TAG, "onResponse: loading top rated list");
                            mSnappingView.onTopRatedLoaded(movies);
                            saveToDb(movies, MoviesDbContract.MovieTypes.TOP_RATED);
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });
    }

    private void getNowPlaying() {
        Log.d(TAG, "getNowPlaying: ");
        mApiService.getNowPlayingMovies()
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> movies = response.body().getResults();
                        if (movies != null) {
                            Log.d(TAG, "onResponse: loading now playing list");
                            mSnappingView.onNowPlayingLoaded(movies);
                            saveToDb(movies, MoviesDbContract.MovieTypes.NOW_PLAYING);
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });
    }

    /**
     * saves the last success query result to DB
     *
     * @param movies result returned from server api
     */
    private void saveToDb(List<Movie> movies, String dbType) {
        for (Movie m : movies) {
            MyApp.getContext().getContentResolver()
                    .insert(MoviesDbContract.Movies.CONTENT_URI, m.getValues(dbType));
        }
    }

}
