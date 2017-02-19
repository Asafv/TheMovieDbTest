package com.asafvaron.themoviedbtest;

import android.app.Application;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // testing MoviesApiEnum

//        MoviesApi.getInstance().init(getApplicationContext());

        // init request queue
        MoviesApiEnum.INSTANCE.getRequestQueue(getApplicationContext());
    }
}
