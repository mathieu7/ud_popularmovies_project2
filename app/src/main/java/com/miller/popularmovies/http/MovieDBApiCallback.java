package com.miller.popularmovies.http;

public interface MovieDBApiCallback {
    void onApiResponse(MovieDBAsyncTask.Result result);
}
