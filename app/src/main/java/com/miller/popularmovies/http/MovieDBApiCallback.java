package com.miller.popularmovies.http;

import android.net.NetworkInfo;

/**
 * Created by Matt on 3/13/2017.
 */

public interface MovieDBApiCallback {
    NetworkInfo getActiveNetworkInfo();
    void onApiResponse(MovieDBAsyncTask.Result result);
}
