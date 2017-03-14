package com.miller.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtils {
    /**
     * A templated String representing movie poster image urls.
     */
    private static final String MOVIE_POSTER_URL_TEMPLATE = "http://image.tmdb.org/t/p/%s/%s";

    /**
     * The default size image to download.
     */
    private static final String DEFAULT_SIZE = "w342";

    /**
     * Return the optimal image size to be downloaded.
     * TODO: implement
     * @return
     */
    private static String getOptimalImageSize() {
        return DEFAULT_SIZE;
    }

    /**
     * Return a formatted URL string for the movie poster of choice.
     * @param posterPath
     * @return
     */
    private static String formatImageURL(@NonNull final String posterPath) {
        return String.format(MOVIE_POSTER_URL_TEMPLATE, getOptimalImageSize(), posterPath);
    }

    /**
     * Helper function to fetch a movie poster image and set onto a given imageView.
     * @param posterPath
     * @param context
     * @param imageView
     */
    public static void setMoviePoster(@NonNull final String posterPath,
                               @NonNull final Context context,
                               @NonNull ImageView imageView)
    {
        Picasso.with(context).load(formatImageURL(posterPath)).into(imageView);
    }
}
