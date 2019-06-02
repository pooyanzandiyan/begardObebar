package com.f22labs.instalikefragmenttransaction.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.f22labs.instalikefragmenttransaction.G;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author alexp
 */
public class NetworkUtils {

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;

    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static OkHttpClient httpClientBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        return httpClient.build();
    }
}
