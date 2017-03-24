package com.miller.popularmovies.providers;

import android.icu.text.PluralRules;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.miller.popularmovies.models.Movie;

public class MovieFavoritesContract {
    public static final String AUTHORITY = "com.miller.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class MovieFavoriteEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static Uri getResourceUri(@NonNull final Movie movie) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES)
                    .appendPath(String.valueOf(movie.getId())).build();
        }

        static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
