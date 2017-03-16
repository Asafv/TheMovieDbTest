package com.asafvaron.themoviedbtest;

import android.app.Application;
import android.content.Context;

import com.asafvaron.themoviedbtest.Utils.Prefs;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MyApp extends Application {

    // FIXME: 16/03/2017 mem leak
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        Prefs.init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }
}
