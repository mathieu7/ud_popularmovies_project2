package com.miller.popularmovies.api;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.miller.popularmovies.R;
import com.miller.popularmovies.models.MoviePreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

    public static Uri.Builder buildApiUri(final MoviePreference preference, Context context) {
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
    /**
     * Review passed back to UI Thread once the task is completed or cancelled.
     */
    public static class ApiResult<T> {
        public T mResponse;
        public Exception mException;
        public ApiResult(T response) {
            mResponse = response;
        }
        public ApiResult(Exception exception) {
            mException = exception;
        }
    }

    /**
     * Defines work to perform on the background thread.
     */

    public <T> ApiResult<T> makeRequest(final String urlString, Class<T> model) {
        ApiResult<T> apiResult = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        T response = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
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
                response = gson.fromJson(result, model);
                apiResult = new ApiResult<T>(response);
            }
        } catch (Exception e) {
            apiResult = new ApiResult<T>(e);
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
