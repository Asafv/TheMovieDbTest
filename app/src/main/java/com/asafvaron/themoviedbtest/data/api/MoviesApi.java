package com.asafvaron.themoviedbtest.data.api;

import com.asafvaron.themoviedbtest.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asafvaron on 20/02/2017.
 */
public class MoviesApi {
    private static final String TAG = "MoviesApi";

    private static MoviesApi sInstance;
    private static Retrofit retrofit = null;

    public static final String API_KEY = "a2ddc78251984ac1b6853aab3885b44c";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    private MoviesService mApi;


    public static MoviesApi getInstance() {
        if (sInstance == null) {
            synchronized (MoviesApi.class) {
                sInstance = new MoviesApi();
            }
        }
        return sInstance;
    }

    public MoviesService getMoviesService() {
        if (mApi == null) createApi();
        return mApi;
    }

    private void createApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new MoviesClientAuthInterceptor(API_KEY)) // get from BuildConfig.GIPHY_API_KEY
                .addInterceptor(logging)
                .build();

        mApi = new Retrofit.Builder()
                .baseUrl(MoviesService.ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(MoviesService.class);
    }

//    public static Retrofit getClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
}
