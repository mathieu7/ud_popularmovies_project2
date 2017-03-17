package com.miller.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    /**
     * Get the active network connection, if any.
     * @param context
     * @return
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }

    /**
     * Return true if the device is connected to a network.
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }

    /**
     * Return true if the device is connected to a Wifi network.
     * @param context
     * @return
     */
    public static boolean isConnectedToWifi(Context context) {
        NetworkInfo activeNetwork = getActiveNetworkInfo(context);
        return activeNetwork != null && activeNetwork.isConnected()
                && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
