package com.miller.popularmovies.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.miller.popularmovies.models.Video;

public class VideoUtils {
    private static final String SITE_YOUTUBE = "youtube";

    /**
     * Utility function to open a youtube video using an implicit intent.
     * @param video
     * @param context
     */
    public static void openYouTubeTrailer(final Video video, Context context){
        if (!video.getSite().equalsIgnoreCase(SITE_YOUTUBE)) {
            return;
        }
        final String videoId = video.getKey();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + videoId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
