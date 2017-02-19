package com.asafvaron.themoviedbtest;

import android.app.Application;
import android.content.Context;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        // testing MoviesApiEnum
        // init request queue
//        MoviesApiEnum.INSTANCE.getRequestQueue(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}
