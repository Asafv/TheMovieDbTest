package com.asafvaron.themoviedbtest.interfaces

interface ResultCallback<T> {
    fun onResult(data: T, err: String)
}