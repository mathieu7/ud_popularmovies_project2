package com.miller.popularmovies.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieFavoritesContract {
    public static final String AUTHORITY = "com.miller.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MovieFavoriteEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
