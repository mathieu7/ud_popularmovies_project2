package com.miller.popularmovies.loader;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.miller.popularmovies.api.MovieDBApiClient;
import com.miller.popularmovies.models.Pagination;

import static android.R.attr.data;
import static com.miller.popularmovies.api.MovieDBApiClient.*;

import java.util.ArrayList;
import java.util.List;

public class MovieDBApiLoader<T> extends AsyncTaskLoader<Result<T>> {
    private List<T> mData;
    private Result<T> mLastResponse;
    private Request<T> mRequest;
    private boolean mIsLoading;
    private int mCurrentPage;

    public MovieDBApiLoader(Request<T> request, Context context) {
        super(context);
        mRequest = request;
    }

    public void setCurrentPage(int page) {
        mCurrentPage = page;
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
        mIsLoading = true;
        MovieDBApiClient client = new MovieDBApiClient();
        return client.makeRequest(mRequest);
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getNextPage() throws Exception {
        if (mLastResponse.mResponse instanceof Pagination) {
            final int nextPage = ((Pagination) mLastResponse.mResponse).getPage() + 1;
            return nextPage;
        }
        throw new Exception("Response type does not extends Pagination model");
    }

    @Override
    public void deliverResult(Result<T> data) {
        if (data != null) {
            if (mData == null)
                mData = new ArrayList<T>();
            mData.add(data.mResponse);
        }
        mLastResponse = data;
        mIsLoading = false;
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
        mIsLoading = false;
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mData = null;
    }
}
