package com.miller.popularmovies.loader;
import android.content.Context;

import com.miller.popularmovies.api.MovieDBApiClient;
import com.miller.popularmovies.models.MovieList;
import com.miller.popularmovies.models.MoviePreference;

public class MovieTrailerLoader extends MovieDBApiLoader<MovieList> {
    private String mUrl;
    private MovieDBApiClient mClient;

    public MovieTrailerLoader(Context context, MoviePreference preference) {
        super(context);
        mUrl = MovieDBApiClient.buildUriString(preference, context);
        mClient = new MovieDBApiClient();
    }

    @Override
    public MovieDBApiClient.ApiResult<MovieList> loadInBackground() {
        return mClient.makeRequest(mUrl, MovieList.class);
    }
}
