package com.yk.okhttp_lib.okhttp;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.itg.lib_log.L;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.yk.okhttp_lib.okhttp.interceptors.*;
import com.yk.okhttp_lib.okhttp.operate.ItgBuilder;

public class ItgOk extends Ok {
    private volatile static ItgOk itgOk;
    private OkHttpClient okHttpClient;

    //开启日志打印拦截器
    private static boolean LOGGER_INTERCEPTOR = true;
    private static LoggerInterceptor.HL LEVEL = null;
    public static String GLOBAL_URL = "";
    private static Application mApplication;


    //在instance()之前调用
    public static void initItgOk(boolean openLogger, LoggerInterceptor.HL loggerLevel, String globalUrl, Application application) {
        LOGGER_INTERCEPTOR = openLogger;
        LEVEL = loggerLevel;
        if (globalUrl != null) {
            char lastChar = globalUrl.toCharArray()[globalUrl.length() - 1];
            if (lastChar != '/') {
                GLOBAL_URL = globalUrl + "/";
            } else {
                GLOBAL_URL = globalUrl;
            }
        }
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(openLogger).append("#").append(loggerLevel).append("#").append(globalUrl);
//        write(application, stringBuilder.toString());
    }

    public static ItgOk instance() {
        if (itgOk == null) {
            synchronized (ItgOk.class) {
                if (itgOk == null) {
                    itgOk = new ItgOk();
                }
            }
        }
        return itgOk;
    }


    private ItgOk() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (LOGGER_INTERCEPTOR) {
            builder.addInterceptor(new LoggerInterceptor(LEVEL));
        }
        okHttpClient = builder.build();

    }


    public ItgBuilder url(String url) {
        request().url(url);
        return request();
    }


    public ItgBuilder header(String name, String value) {
        request().header(name, value);
        return request();
    }


    public ItgBuilder addHeader(String name, String value) {
        request().addHeader(name, value);
        return request();
    }

    public ItgBuilder removeHeader(String name) {
        request().removeHeader(name);
        return request();
    }

    public ItgBuilder headers(Headers headers) {
        request().headers(headers);
        return request();
    }


    public ItgBuilder cacheControl(CacheControl cacheControl) {
        request().cacheControl(cacheControl);
        return request();
    }


    public ItgBuilder tag(@Nullable Object tag) {
        request().tag(tag);
        return request();
    }


    public <T> ItgBuilder tag(Class<? super T> type, @Nullable T tag) {
        request().tag(tag);
        return request();
    }


    public ItgBuilder method(String method) {
        request().method(method);
        return request();
    }


    public ItgBuilder autoCancel(Lifecycle lifecycle) {
        request().autoCancel(lifecycle);
        return request();
    }


    public ItgBuilder addMultiFile(String name, String file) {
        addMultiFile(name, null, null, file);
        return request();
    }


    public ItgBuilder addMultiFile(String name, File file) {
        addMultiFile(name, null, null, file);
        return request();
    }


    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, File file) {
        request().addMultiFile(name, mediaType, fileName, file);
        return request();
    }


    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, String file) {
        request().addMultiFile(name, mediaType, fileName, file);
        return request();
    }


    public ItgBuilder addMultiParams(String key, String param) {
        request().addMultiFile(key, param);
        return request();
    }


    public ItgBuilder addMultiJson(String key, Object json) {
        request().addMultiJson(key, json);
        return request();
    }


    public ItgBuilder addParams(String key, String params) {
        request().addParams(key, params);
        return request();
    }

    public ItgBuilder addParams(String key, int params) {
        addParams(key, String.valueOf(params));
        return request();
    }

    public ItgBuilder addParams(String key, double params) {
        addParams(key, String.valueOf(params));
        return request();
    }

    public ItgBuilder addParams(String key, float params) {
        addParams(key, String.valueOf(params));
        return request();
    }


    public ItgBuilder addFile(String file) {
        request().addFile(file);
        return request();
    }


    public ItgBuilder addFile(File file) {
        request().addFile(file);
        return request();
    }


    public ItgBuilder addJson(Object obj) {
        request().addJson(obj);
        return request();
    }


    public ItgBuilder addContent(String content) {
        request().addContent(content);
        return request();
    }

    @Override
    public void go(Callback callback) {
        Request request = request().build();
        L.e(request.url().toString());
        final Call call = okHttpClient.newCall(request);
        request().autoCancel(new ItgRequest.CancelRequestObserver(call));
        releaseBuild();
        call.enqueue(callback);
    }

    private static void write(Application application, String msg) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = application.openFileOutput("globalUrl.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
