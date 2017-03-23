package com.miller.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.miller.popularmovies.api.MovieDBApiClient;

import java.util.ArrayList;
import java.util.List;


public abstract class MovieDBApiLoader<T> extends AsyncTaskLoader<MovieDBApiClient.ApiResult<T>> {
    private List<T> mData;
    private MovieDBApiClient.ApiResult<T> mLastResponse;
    protected int mCurrentPage, mTotalPages, mItemsPerPage;

    public MovieDBApiLoader(Context context) {
        super(context);
        init();
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
    public abstract MovieDBApiClient.ApiResult<T> loadInBackground();

    @Override
    public void deliverResult(MovieDBApiClient.ApiResult<T> data) {
        if (data != null) {
            if (mData == null)
                mData = new ArrayList<T>();
            mData.add(data.mResponse);
        }
        mLastResponse = data;
        super.deliverResult(data);
    }

    private void init() {
        mCurrentPage = 0;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public int getItemsPerPage() {
        return mItemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        mItemsPerPage = itemsPerPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPage(int totalPages) {
        mTotalPages = totalPages;
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

    public boolean getHasMoreResults() {
        return (mCurrentPage <= mTotalPages);
    }
}
