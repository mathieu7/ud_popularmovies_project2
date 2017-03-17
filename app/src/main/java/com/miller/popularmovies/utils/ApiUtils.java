package com.miller.popularmovies.utils;

import android.content.Context;
import android.net.Uri;

import com.miller.popularmovies.R;
import com.miller.popularmovies.models.MoviePreference;

public class ApiUtils {
    private static final String API_HOST = "https://api.themoviedb.org/";
    private static final String API_VERSION = "3";
    private static final String API_PATH = "movie";
    private static final String API_KEY_PARAM_NAME = "api_key";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String PAGE_PARAM_NAME = "page";

    private static Uri.Builder buildApiUri(final MoviePreference preference, Context context) {
        Uri.Builder builder = Uri.parse(API_HOST)
                .buildUpon()
                .appendPath(API_VERSION)
                .appendPath(API_PATH);

        switch (preference) {
            case MOST_POPULAR:
                builder.appendPath(POPULAR);
                break;
            case TOP_RATED:
                builder.appendPath(TOP_RATED);
                break;
        }

        String apiKey = context.getString(R.string.API_KEY);
        builder.appendQueryParameter(API_KEY_PARAM_NAME, apiKey);
        return builder;
    }
    /**
     * Utility function to build a specific Movie DB API URL string, given a movie preference.
     * @param preference
     * @return
     */
    public static String buildUriString(final MoviePreference preference, Context context) {
        return buildApiUri(preference, context).build().toString();
    }

    /**
     * Utility function to build a paginated URL string to the Movie DB API.
     * @param preference
     * @param context
     * @param page
     * @return
     */
    public static String buildUriString(final MoviePreference preference, Context context, final int page) {
        Uri.Builder builder = buildApiUri(preference, context);
        builder.appendQueryParameter(PAGE_PARAM_NAME, Integer.toString(page));
        return builder.build().toString();
    }
}
