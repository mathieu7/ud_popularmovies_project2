package com.miller.popularmovies.http;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.miller.popularmovies.models.ApiResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * AsyncTask to download the movie results from a specific MovieDB endpoint.
 */
public class MovieDBAsyncTask extends AsyncTask<String, Void, MovieDBAsyncTask.Result> {

    /**
     * Result passed back to UI Thread once the task is completed or cancelled.
     */
    public static class Result {
        public ApiResponse mResponse;
        public Exception mException;
        Result(ApiResponse response) {
            mResponse = response;
        }
        Result(Exception exception) {
            mException = exception;
        }
    }

    public MovieDBAsyncTask(MovieDBApiCallback callback) {
        mCallback = callback;
    }

    private MovieDBApiCallback mCallback;
    /**
     * Defines work to perform on the background thread.
     */
    @Override
    protected Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                ApiResponse response = executeDownload(url);
                if (response != null) {
                    result = new Result(response);
                } else {
                    throw new IOException("No response received.");
                }
            } catch (Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }

    /**
     * Download the content.
     * @param url
     * @return
     * @throws IOException
     */
    private ApiResponse executeDownload(final URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        ApiResponse response = null;
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
                response = gson.fromJson(result, ApiResponse.class);
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
        return response;
    }

    /**
     * Updates the DownloadCallback with the result.
     */
    @Override
    protected void onPostExecute(Result result) {
        if (result != null && mCallback != null) {
            mCallback.onApiResponse(result);
        }
    }

    /**
     * Override to add special behavior for cancelled AsyncTask.
     */
    @Override
    protected void onCancelled(Result result) {
    }
}
