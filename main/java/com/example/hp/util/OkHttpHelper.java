package com.example.hp.util;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpHelper {

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)//设置超时时间
            .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(5, TimeUnit.SECONDS)//设置写入超时时间
            .build();

    public static void SendOkHttpRequest(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    public static void PostOkHttpRequest(Request request ,Callback callback){
        client.newCall(request).enqueue(callback);
    }

}
