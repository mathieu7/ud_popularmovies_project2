package com.miller.popularmovies.utils;

import android.net.Uri;

import com.miller.popularmovies.models.MoviePreference;

public class ApiUtils {
    private static final String API_KEY = "";
    private static final String API_HOST = "http://api.themovedb.org/";
    private static final String API_VERSION = "3";
    private static final String API_PATH = "movie";
    private static final String API_KEY_PARAM_NAME = "api_key";

    /**
     * Utility function to build a specific Movie DB API Url, given a movie preference.
     * @param preference
     * @return
     */
    public static String buildUriString(final MoviePreference preference) {
        Uri.Builder builder = Uri.parse(API_HOST)
                .buildUpon()
                .appendPath(API_VERSION)
                .appendPath(API_PATH);

        switch (preference) {
            case MOST_POPULAR:
                builder.appendPath("popular");
                break;
            case TOP_RATED:
                builder.appendPath("top_rated");
                break;
        }

        builder.appendQueryParameter(API_KEY_PARAM_NAME, API_KEY);
        return builder.build().toString();
    }
}
