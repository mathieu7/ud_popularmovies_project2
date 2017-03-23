package com.miller.popularmovies.loader;
import android.content.Context;

import com.google.gson.Gson;
import com.miller.popularmovies.api.ApiUtils;
import com.miller.popularmovies.api.MovieDBApiClient;
import com.miller.popularmovies.models.MovieList;
import com.miller.popularmovies.models.MoviePreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MovieListLoader extends MovieDBApiLoader<MovieList> {
    private String mUrl;

    public MovieListLoader(Context context, MoviePreference preference) {
        super(context);
        mUrl = ApiUtils.buildUriString(preference, context);
    }

    @Override
    public MovieDBApiClient.ApiResult<MovieList> loadInBackground() {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        MovieList response = null;
        MovieDBApiClient.ApiResult<MovieList> result;
        try {
            URL url = new URL(mUrl);
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
                String res = resultStringBuilder.toString();
                Gson gson = new Gson();
                response = gson.fromJson(res, MovieList.class);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            try {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new MovieDBApiClient.ApiResult<>(response);
    }
}
