package com.plugin.androidbase;

import android.app.Application;

import com.plugin.okhttp_lib.okhttp.ItgOk;
import com.plugin.okhttp_lib.okhttp.interceptors.LoggerInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new ItgOk.Builder().
                 globalUrl("http://47.100.4.54:8080")
                .openHttpLog(true)
                .logLevel(LoggerInterceptor.HL.BODY)
                .build(this);

    }
}
