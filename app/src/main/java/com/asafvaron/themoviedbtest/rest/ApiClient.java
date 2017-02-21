package com.asafvaron.themoviedbtest.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asafvaron on 20/02/2017.
 */
public class ApiClient {
    private static final String TAG = ApiClient.class.getSimpleName();

    public static final String API_KEY = "a2ddc78251984ac1b6853aab3885b44c";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
