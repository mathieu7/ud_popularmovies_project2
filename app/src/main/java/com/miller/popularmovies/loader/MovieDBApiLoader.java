package com.miller.popularmovies.loader;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.miller.popularmovies.api.MovieDBApiClient;

import java.util.ArrayList;
import java.util.List;

public class MovieDBApiLoader<T> extends AsyncTaskLoader<MovieDBApiClient.Result<T>> {
    private List<T> mData;
    private MovieDBApiClient.Result<T> mLastResponse;
    private MovieDBApiClient.Request<T> mRequest;

    public MovieDBApiLoader(MovieDBApiClient.Request<T> request, Context context) {
        super(context);
        mRequest = request;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mLastResponse);
            return;
        }
        forceLoad();
    }

    @Override
    public MovieDBApiClient.Result<T> loadInBackground() {
        MovieDBApiClient client = new MovieDBApiClient();
        return client.makeRequest(mRequest);
    }

    @Override
    public void deliverResult(MovieDBApiClient.Result<T> data) {
        if (data != null) {
            if (mData == null)
                mData = new ArrayList<T>();
            mData.add(data.mResponse);
        }
        mLastResponse = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mData = null;
    }
}
