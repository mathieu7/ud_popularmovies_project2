package com.miller.popularmovies.api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

/**
 * AsyncTask to download the movie results from a specific MovieDB endpoint.
 */
public class MovieDBApiClient {

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

    /*public <T> ApiResult<T> makeRequest(T model) throws IOException {
        ApiResult<T> apiResult;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        T response = null;
        try {
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
                response = gson.fromJson(result, T.class);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return apiResult;
    }*/
}
