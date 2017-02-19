package com.asafvaron.themoviedbtest.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesContract {

    // table name
    public static final String TABLE_NAME = "movies";

    //the provider's authority
    public final static String AUTHORITY = "com.asafvaron.themoviedbtest.provider";

    // To prevent someone from accidentally instantiating the contract class,
    private MoviesContract() {
    }

    // movie table
    public static class Movies implements BaseColumns {

        //uri
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RUNTIME = "runtime";
    }
}