package com.miller.popularmovies.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.miller.popularmovies.models.MovieList;

public class MovieDBApiLoader extends AsyncTaskLoader<MovieList> {
    private MovieList mData;
    private int mCurrentPage, mTotalPages, mItemsPerPage;

    public MovieDBApiLoader(Context context) {
        super(context);
        init();
    }
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
            return;
        }
        forceLoad();
    }

    @Override
    public MovieList loadInBackground() {
        return null;
    }

    @Override
    public void deliverResult(MovieList data) {

        if( data != null )
        {
            if( mData == null )
                mData = data;
            //else
              //  mData.
        }
        super.deliverResult(data);
       // super.deliverResult( ( ( mData == null ) ? null : new ArrayList<D>( mData ) ) );
    }

    private void init()
    {
        mCurrentPage = 0;
    }

    public int getCurrentPage( )
    {
        return mCurrentPage;
    }

    public void setCurrentPage( int currentPage )
    {
        mCurrentPage = currentPage;
    }

    public int getItemsPerPage( )
    {
        return mItemsPerPage;
    }

    public void setItemsPerPage( int itemsPerPage )
    {
        mItemsPerPage = itemsPerPage;
    }

    public int getTotalPages( )
    {
        return mTotalPages;
    }

    public void setTotalPage( int totalPages )
    {
        mTotalPages = totalPages;
    }

    @Override
    protected void onStopLoading()
    {
        cancelLoad();
    }

    @Override
    protected void onReset()
    {
        super.onReset();
        onStopLoading();
        mData = null;
    }

    public boolean getHasMoreResults( )
    {
        return( mCurrentPage <= mTotalPages );
    }
}
