package com.miller.popularmovies.loader;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.miller.popularmovies.api.MovieDBApiClient;
import static com.miller.popularmovies.api.MovieDBApiClient.*;

import java.util.ArrayList;
import java.util.List;

public class MovieDBApiLoader<T> extends AsyncTaskLoader<Result<T>> {
    private List<T> mData;
    private Result<T> mLastResponse;
    private Request<T> mRequest;

    public MovieDBApiLoader(Request<T> request, Context context) {
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
    public Result<T> loadInBackground() {
        MovieDBApiClient client = new MovieDBApiClient();
        return client.makeRequest(mRequest);
    }

    @Override
    public void deliverResult(Result<T> data) {
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
