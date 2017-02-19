package com.asafvaron.themoviedbtest.listeners;

public interface ResultCallback<T> {
    void onResult(T data, String err);
}