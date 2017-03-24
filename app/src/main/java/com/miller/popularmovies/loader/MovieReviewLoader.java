package com.miller.popularmovies.loader;
import android.content.Context;

import static com.miller.popularmovies.api.MovieDBApiClient.ApiResult;
import com.miller.popularmovies.api.MovieDBApiClient;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MovieReviews;

public class MovieReviewLoader extends MovieDBApiLoader<MovieReviews> {
    private String mUrl;
    private MovieDBApiClient mClient;

    public MovieReviewLoader(Context context, Movie movie) {
        super(context);
        mClient = new MovieDBApiClient();
    }

    @Override
    public ApiResult<MovieReviews> loadInBackground() {
        return mClient.makeRequest(mUrl, MovieReviews.class);
    }
}
