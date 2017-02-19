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

public class MoviesApi {
    private static final String TAG = MoviesApi.class.getSimpleName();

    private static MoviesApi sInstance = null;

    private RequestQueue mRequestQueue;
    private Context mContext;

    private MoviesApi() {
    }

    public static synchronized MoviesApi getInstance() {
        if (sInstance == null)
            sInstance = new MoviesApi();
        return sInstance;
    }

    public void init(Context context) {
        mContext = context;
        // prep some search data
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mContext);
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.d(TAG, "addToRequestQueue: " + req.toString() + " request tag: " + tag);

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d(TAG, "addToRequestQueue: " + req.toString());
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        Log.d(TAG, "cancelPendingRequests: " + tag.toString());
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
