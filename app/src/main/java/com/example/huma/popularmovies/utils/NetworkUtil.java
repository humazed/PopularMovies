package com.example.huma.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * User: YourPc
 * Date: 4/1/2017
 */

public final class NetworkUtil {

    @NonNull
    public static Interceptor getCacheInterceptor() {
        return chain -> {
            Request request = chain.request();
            if (NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 60).build();
            } else {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
            }
            return chain.proceed(request);
        };
    }

    @NonNull
    public static OkHttpClient getCaCheOkHttpClient(Context context) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024)) // 10 MB
                .addInterceptor(getCacheInterceptor())
                .build();
    }

}
