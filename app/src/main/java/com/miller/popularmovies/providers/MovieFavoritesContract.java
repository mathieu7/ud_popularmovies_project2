package com.miller.popularmovies.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieFavoritesContract {
    public static final String AUTHORITY = "com.miller.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    /* TaskEntry is an inner class that defines the contents of the task table */
    static final class MovieFavoriteEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // Task table and column names
        static final String TABLE_NAME = "favorites";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        static final String COLUMN_MOVIE_TITLE = "movie_title";
        static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
