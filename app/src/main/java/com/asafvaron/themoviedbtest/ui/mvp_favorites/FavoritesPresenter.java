package com.asafvaron.themoviedbtest.ui.mvp_favorites;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.asafvaron.themoviedbtest.MyApp;
import com.asafvaron.themoviedbtest.data.sql_db.MoviesDbContract;
import com.asafvaron.themoviedbtest.model.database.MoviesContract;
import com.asafvaron.themoviedbtest.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by asafvaron on 05/03/2017.
 */
class FavoritesPresenter implements FavoritesContract.UserActions,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "FavoritesPresenter";

    private static final int FAVS_LOADER_ID = 1;

    private final LoaderManager mLoaderManager;
    private final FavoritesContract.View mFavsView;
    private List<Movie> mFavsList = new ArrayList<>();

    FavoritesPresenter(@NonNull LoaderManager loaderManager, @NonNull FavoritesContract.View
            favsView) {
        mLoaderManager = checkNotNull(loaderManager, "LoaderManager cannot be null!");
        mFavsView = checkNotNull(favsView, "Favorites View cannot be null!");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");

        // return a new loader for favorites db
        return new CursorLoader(MyApp.getContext(), MoviesDbContract.Favorites.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Log.d(TAG, "onLoadFinished: ");
        while (c.moveToNext()) {
            Movie m = new Movie();
            m.setId(c.getInt(c.getColumnIndex(MoviesContract.Favorites.COLUMN_MOVIE_ID)));
            m.setTitle(c.getString(c.getColumnIndex(MoviesContract.Favorites.COLUMN_TITLE)));
            m.setOriginalTitle(c.getString(c.getColumnIndex(MoviesContract.Favorites.COLUMN_ORIGINAL_TITLE)));
            m.setOverview(c.getString(c.getColumnIndex(MoviesContract.Favorites.COLUMN_OVERVIEW)));
            m.setVoteAverage(c.getDouble(c.getColumnIndex(MoviesContract.Favorites.COLUMN_VOTE_AVERAGE)));
            m.setVoteCount(c.getInt(c.getColumnIndex(MoviesContract.Favorites.COLUMN_VOTE_COUNT)));
            m.setPosterPath(c.getString(c.getColumnIndex(MoviesContract.Favorites.COLUMN_POSTER)));
            m.setReleaseDate(c.getString(c.getColumnIndex(MoviesContract.Favorites.COLUMN_RELEASE_DATE)));
            // create the list
            mFavsList.add(m);
        }

        // finished loading
        mFavsView.updateProgress(false);
        mFavsView.onLoadComplete(mFavsList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // no - op
    }

    @Override
    public void loadFavorites() {
        // update the progress
        mFavsView.updateProgress(true);

        // data already exists return to ui
        if (mFavsList.size() > 0) {
            mFavsView.updateProgress(false);
            mFavsView.onLoadComplete(mFavsList);
        } else {
            // no data - fetch from db
            mFavsList = new ArrayList<>();
            mLoaderManager.initLoader(FAVS_LOADER_ID, null, this);
        }
    }
}
