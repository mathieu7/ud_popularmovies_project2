package com.miller.popularmovies.api;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.miller.popularmovies.R;
import com.miller.popularmovies.models.Movie;
import com.miller.popularmovies.models.MovieList;
import com.miller.popularmovies.models.MoviePreference;
import com.miller.popularmovies.models.MovieVideos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static com.miller.popularmovies.models.MoviePreference.MOST_POPULAR;

public class MovieDBApiClient {

    private static final String API_HOST = "https://api.themoviedb.org/";
    private static final String API_VERSION = "3";
    private static final String API_PATH = "movie";
    private static final String API_KEY_PARAM_NAME = "api_key";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";
    private static final String PAGE_PARAM_NAME = "page";

    private static final int TIMEOUT = 3000;

    private static String createUrl(@NonNull final String endpoint,
                                    @Nullable Movie movie,
                                    @NonNull Context context) {
        Uri.Builder builder = Uri.parse(API_HOST)
                .buildUpon()
                .appendPath(API_VERSION)
                .appendPath(API_PATH);

        switch (endpoint) {
            case POPULAR:
                builder.appendPath(POPULAR);
                break;
            case TOP_RATED:
                builder.appendPath(TOP_RATED);
                break;
            case VIDEOS:
                if (movie == null)
                    throw new IllegalArgumentException("Videos endpoint needs a Movie parameter");
                builder.appendPath(String.valueOf(movie.getId())).appendPath(VIDEOS);
                break;
            case REVIEWS:
                if (movie == null)
                    throw new IllegalArgumentException("Reviews endpoint needs a Movie parameter");
                builder.appendPath(String.valueOf(movie.getId())).appendPath(REVIEWS);
        }

        String apiKey = context.getString(R.string.API_KEY);
        builder.appendQueryParameter(API_KEY_PARAM_NAME, apiKey);
        return builder.build().toString();
    }

    /**
     * API result wrapper passed back to UI Thread once the task is completed or cancelled.
     */
    public static class Result<T> {
        public T mResponse;
        public Exception mException;
        public Result(T response) {
            mResponse = response;
        }
        public Result(Exception exception) {
            mException = exception;
        }
    }

    public abstract static class Request<T> {
        public Class<T> classType;
        protected String url;
        protected String path;

        public String getUrl() {
            return url;
        }
    }

    public static class VideoRequest extends Request<MovieVideos> {
        public VideoRequest(final Movie movie, final Context context) {
            path = VIDEOS;
            url = createUrl(path, movie, context);
            classType = MovieVideos.class;
        }
    }

    public static class PopularRequest extends Request<MovieList> {
        public PopularRequest(final Context context) {
            path = POPULAR;
            url = createUrl(path, null, context);
            classType = MovieList.class;
        }
    }

    public static class TopRatedRequest extends Request<MovieList> {
        public TopRatedRequest(final Context context) {
            path = POPULAR;
            url = createUrl(path, null, context);
            classType = MovieList.class;
        }
    }

    /**
     * Defines work to perform on the background thread.
     */
    public <T> Result<T> makeRequest(Request<T> request) {
        Result<T> apiResult = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        T response = null;
        try {
            URL url = new URL(request.getUrl());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                StringBuilder resultStringBuilder = new StringBuilder();
                String buffer;
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                while ((buffer = reader.readLine()) != null) {
                    resultStringBuilder.append(buffer);
                }
                String result = resultStringBuilder.toString();
                Gson gson = new Gson();
                response = gson.fromJson(result, request.classType);
                apiResult = new Result<T>(response);
            }
        } catch (Exception e) {
            apiResult = new Result<T>(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apiResult;
    }
}
