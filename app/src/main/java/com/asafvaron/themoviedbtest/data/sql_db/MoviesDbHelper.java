package com.asafvaron.themoviedbtest.data.sql_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asafvaron on 19/02/2017.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "MoviesDbHelper";

    public static final int DB_VERSION = 7;
    public static final String DB_NAME = "theMoviesDbTest.db";

    // create table
    private static final String SQL_CREATE_MOVIES_ENTRIES =
            "CREATE TABLE " + MoviesDbContract.Movies.TABLE_NAME + " (" +
                    MoviesDbContract.Movies._ID + " INTEGER PRIMARY KEY," +
                    MoviesDbContract.Movies.COLUMN_MOVIE_ID + " REAL," +
                    MoviesDbContract.Movies.COLUMN_TITLE + " TEXT," +
                    MoviesDbContract.Movies.COLUMN_ORIGINAL_TITLE + " TEXT," +
                    MoviesDbContract.Movies.COLUMN_RELEASE_DATE + " TEXT," +
                    MoviesDbContract.Movies.COLUMN_OVERVIEW + " TEXT," +
                    MoviesDbContract.Movies.COLUMN_POSTER + " TEXT," +
                    MoviesDbContract.Movies.COLUMN_TYPE + " TEXT," +
//                    MoviesDbContract.Movies.COLUMN_POSTER + " BLOB," +
                    MoviesDbContract.Movies.COLUMN_VOTE_AVERAGE + " REAL," +
                    MoviesDbContract.Movies.COLUMN_VOTE_COUNT + " REAL," +
                    MoviesDbContract.Movies.COLUMN_IS_IN_FAVS + " REAL," +
                    MoviesDbContract.Movies.COLUMN_RUNTIME + " REAL)";


    // create favorites table
    private static final String SQL_CREATE_FAVORITES_ENTRIES =
            "CREATE TABLE " + MoviesDbContract.Favorites.TABLE_NAME + " (" +
                    MoviesDbContract.Favorites._ID + " INTEGER PRIMARY KEY," +
                    MoviesDbContract.Favorites.COLUMN_MOVIE_ID + " REAL," +
                    MoviesDbContract.Favorites.COLUMN_TITLE + " TEXT," +
                    MoviesDbContract.Favorites.COLUMN_ORIGINAL_TITLE + " TEXT," +
                    MoviesDbContract.Favorites.COLUMN_RELEASE_DATE + " TEXT," +
                    MoviesDbContract.Favorites.COLUMN_OVERVIEW + " TEXT," +
                    MoviesDbContract.Favorites.COLUMN_POSTER + " TEXT," +
                    MoviesDbContract.Favorites.COLUMN_TYPE + " TEXT," +
//                    MoviesDbContract.Favorites.COLUMN_POSTER + " BLOB," +
                    MoviesDbContract.Favorites.COLUMN_VOTE_AVERAGE + " REAL," +
                    MoviesDbContract.Favorites.COLUMN_VOTE_COUNT + " REAL," +
                    MoviesDbContract.Favorites.COLUMN_RUNTIME + " REAL)";

    // delete movies table
    private static final String SQL_DELETE_MOVIES_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesDbContract.Movies.TABLE_NAME;

    private static final String SQL_DELETE_FAVORITES_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesDbContract.Favorites.TABLE_NAME;

    public MoviesDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIES_ENTRIES);
        db.execSQL(SQL_CREATE_FAVORITES_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_MOVIES_ENTRIES);
        db.execSQL(SQL_DELETE_FAVORITES_ENTRIES);
        // XXX ask Yossi - is there a better way to upgrade table instead of deleting the previous one
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
