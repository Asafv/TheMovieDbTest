package com.asafvaron.themoviedbtest.mvp_info;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.asafvaron.themoviedbtest.Utils.Consts;
import com.asafvaron.themoviedbtest.Utils.Prefs;
import com.asafvaron.themoviedbtest.database.movies.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;
import com.asafvaron.themoviedbtest.rest.ApiClient;
import com.asafvaron.themoviedbtest.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 19/03/2017.
 */

class InfoModel {

    private static final String TAG = "InfoModel";

    private final Movie mMovie;
    private ApiInterface mApiInterface;
    private Call<Movie> mCall;


    interface InfoModelListener {
        interface RunTimeCallback {
            void onSuccess(int runTime);

            void onFailed(String err);
        }

        interface UpdateDbCallback {
            void onUpdated();

            void onFailed(String err);
        }
    }

    // will be created for each movie
    InfoModel(Movie movie) {
        mMovie = checkNotNull(movie, "Movie object cannot be null!!");
        init();
    }

    private void init() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mCall = mApiInterface.getMovieDetails(mMovie.getId(), ApiClient.API_KEY);
    }

    void getRunTime(final InfoModelListener.RunTimeCallback runTimeCallback) {
//        Log.d(TAG, "getMovieRunTime: url: " + mCall.request().url());
        mCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                runTimeCallback.onSuccess(response.body().getRunTime());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                runTimeCallback.onFailed(t.getLocalizedMessage());
            }
        });
    }

    void updateMovieToDb(Context context, Movie movie,
                         InfoModelListener.UpdateDbCallback updateDbCallback) {

        ContentResolver cr = context.getContentResolver();

        // update the single movie entry ONLY!
//        String where = ;

        int rowsUpdated = cr.update(MoviesContract.Movies.CONTENT_URI,
                movie.getValues(Prefs.getInstance().getString(Consts.LAST_DB_TYPE)),
                MoviesContract.Movies.COLUMN_MOVIE_ID + "=" + movie.getId(), null);

        Log.e(TAG, "updateMovieToDb: rowsUpdated: " + rowsUpdated);
        if (rowsUpdated == 1) {
            updateDbCallback.onUpdated();
        } else {
            updateDbCallback.onFailed("Should not update more then 1 row");
        }
    }

}
