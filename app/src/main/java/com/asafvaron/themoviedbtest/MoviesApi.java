package com.asafvaron.themoviedbtest;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.asafvaron.themoviedbtest.database.MoviesContract;
import com.asafvaron.themoviedbtest.listeners.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asafvaron on 19/02/2017.
 */

public class MoviesApi {
    private static final String TAG = MoviesApi.class.getSimpleName();

    private static final String API_KEY = "?api_key=a2ddc78251984ac1b6853aab3885b44c";
    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/"; // add .jpg
    private static final String NOW_PLAYING_MOVIES_URL = API_URL + "now_playing" + API_KEY;


    private static MoviesApi sInstance = null;

    private RequestQueue mRequestQueue;

    private MoviesApi() {
    }

    public static synchronized MoviesApi getInstance() {
        if (sInstance == null)
            sInstance = new MoviesApi();
        return sInstance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        Log.d(TAG, "addToRequestQueue: " + req.toString() + " request tag: " + tag);

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(MyApp.getContext()).add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d(TAG, "addToRequestQueue: " + req.toString());
        getRequestQueue(MyApp.getContext()).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        Log.d(TAG, "cancelPendingRequests: " + tag.toString());
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void getSomeMovies(final ResultCallback<Boolean> resultCallback) {
        Log.d(TAG, "getSomeMovies: ");
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                NOW_PLAYING_MOVIES_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, "onResponse() returned: " + response);
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
                        resultCallback.onResult(true, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse() returned: " + error.getLocalizedMessage());
                        resultCallback.onResult(false, error.getLocalizedMessage());
                    }
                }
        );

        getRequestQueue(MyApp.getContext()).add(req);
    }

    private void addMovieToDb(JSONObject obj) {
        Log.d(TAG, "addMovieToDb: " + obj.toString());

        ContentValues values = new ContentValues();
        try {
            if (obj.has("id"))
                values.put(MoviesContract.Movies.COLUMN_MOVIE_ID, obj.getInt("id"));

            if (obj.has("title"))
                values.put(MoviesContract.Movies.COLUMN_TITLE, obj.getString("title"));

            if (obj.has("original_title"))
                values.put(MoviesContract.Movies.COLUMN_ORIGINAL_TITLE, obj.getString("original_title"));

            if (obj.has("overview"))
                values.put(MoviesContract.Movies.COLUMN_OVERVIEW, obj.getString("overview"));

            if (obj.has("runtime"))
                values.put(MoviesContract.Movies.COLUMN_RUNTIME, obj.getInt("runtime"));

            if (obj.has("vote_average"))
                values.put(MoviesContract.Movies.COLUMN_VOTE_AVERAGE, obj.getInt("vote_average"));

            if (obj.has("vote_count"))
                values.put(MoviesContract.Movies.COLUMN_VOTE_COUNT, obj.getInt("vote_count"));

            if (obj.has("poster_path"))
                values.put(MoviesContract.Movies.COLUMN_POSTER, IMAGE_URL + obj.getString("poster_path"));

            if (obj.has("release_date"))
                values.put(MoviesContract.Movies.COLUMN_RELEASE_DATE, obj.getString("release_date"));

            MyApp.getContext().getContentResolver().insert(MoviesContract.Movies.CONTENT_URI, values);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
