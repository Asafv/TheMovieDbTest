package com.asafvaron.themoviedbtest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asafvaron on 19/02/2017.
 */

//XXX ask Yossi - why we shouldn't use enum as a singleTon pattern
    // it worked fine with network requests
public enum MoviesApiEnum {
    INSTANCE;

    private static final String TAG = MoviesApiEnum.class.getSimpleName();

    private static final String API_KEY = "?api_key=a2ddc78251984ac1b6853aab3885b44c";
    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/"; // add .jpg
    private static final String NOW_PLAYING_MOVIES_URL = API_URL + "now_playing" + API_KEY;


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

    public void getSomeMovies() {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                NOW_PLAYING_MOVIES_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() returned: " + response);
                        if (response.has("results"))
                            try {
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject obj = results.getJSONObject(i);
                                    if (obj != null) {
                                        addMovieToDb(obj);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse() returned: " + error.getLocalizedMessage());
                    }
                }
        );

        mRequestQueue.add(req);
    }

    private void addMovieToDb(JSONObject obj) {

    }
}
