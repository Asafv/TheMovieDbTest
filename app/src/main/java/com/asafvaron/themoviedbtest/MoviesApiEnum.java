package com.asafvaron.themoviedbtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by asafvaron on 19/02/2017.
 */

public enum MoviesApiEnum {
    INSTANCE;

    private static final String TAG = MoviesApiEnum.class.getSimpleName();

    private static final String API_KEY = "?api_key=a2ddc78251984ac1b6853aab3885b44c";
    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/"; // add .jpg
    private static final String NOW_PLAYING_MOVIES_URL = API_URL + "now_playing";


    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.d(TAG, "addToRequestQueue: " + req.toString() + " request tag: " + tag);

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        mRequestQueue.add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d(TAG, "addToRequestQueue: " + req.toString());
        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(Object tag) {
        Log.d(TAG, "cancelPendingRequests: " + tag.toString());
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
