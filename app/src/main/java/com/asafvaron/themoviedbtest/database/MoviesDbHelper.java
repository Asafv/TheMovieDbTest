package com.asafvaron.themoviedbtest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MoviesDbHelper.class.getSimpleName();

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "theMoviesDbTest.db";

    // create table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MoviesContract.TopRatedMovies.TABLE_NAME + " (" +
                    MoviesContract.TopRatedMovies._ID + " INTEGER PRIMARY KEY," +
                    MoviesContract.TopRatedMovies.COLUMN_MOVIE_ID + " REAL," +
                    MoviesContract.TopRatedMovies.COLUMN_TITLE + " TEXT," +
                    MoviesContract.TopRatedMovies.COLUMN_ORIGINAL_TITLE + " TEXT," +
                    MoviesContract.TopRatedMovies.COLUMN_RELEASE_DATE + " TEXT," +
                    MoviesContract.TopRatedMovies.COLUMN_OVERVIEW + " TEXT," +
                    MoviesContract.TopRatedMovies.COLUMN_POSTER + " TEXT," +
//                    MoviesContract.TopRatedMovies.COLUMN_POSTER + " BLOB," +
                    MoviesContract.TopRatedMovies.COLUMN_VOTE_AVERAGE + " REAL," +
                    MoviesContract.TopRatedMovies.COLUMN_VOTE_COUNT + " REAL," +
                    MoviesContract.TopRatedMovies.COLUMN_RUNTIME + " REAL)";

    // delete table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesContract.TopRatedMovies.TABLE_NAME;

    public MoviesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        // XXX ask Yossi - is there a better way to upgrade table instead of deleting the previous one
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
