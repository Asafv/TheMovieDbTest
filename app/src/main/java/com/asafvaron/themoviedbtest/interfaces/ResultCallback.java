package com.asafvaron.themoviedbtest.interfaces;

public interface ResultCallback<T> {
    void onResult(T data, String err);
}