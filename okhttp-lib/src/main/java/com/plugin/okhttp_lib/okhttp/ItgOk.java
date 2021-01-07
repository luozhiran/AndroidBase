package com.plugin.okhttp_lib.okhttp;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.itg.lib_log.L;

import java.io.File;
import java.io.IOException;


import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.plugin.okhttp_lib.okhttp.interceptors.*;

public class ItgOk extends Ok {
    private volatile static ItgOk itgOk;
    private OkHttpClient okHttpClient;
    protected String url;
    private Application application;


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
        okHttpClient = builder.build();
    }

    private ItgOk(Builder builder) {
        url = builder.globalUrl;
        OkHttpClient.Builder okbuilder = new OkHttpClient.Builder();
        if (builder.interceptors != null) {
            for (Interceptor i : builder.interceptors) {
                okbuilder.addInterceptor(i);
            }
        }
        if (builder.openLog) {
            okbuilder.addInterceptor(new LoggerInterceptor(builder.level, builder.writePath));
        }
        this.application = builder.application;
        okHttpClient = okbuilder.build();
    }


    public ItgOk url(String url) {
        request().url(url);
        return this;
    }


    public ItgOk header(String name, String value) {
        request().header(name, value);
        return this;
    }


    public ItgOk addHeader(String name, String value) {
        request().addHeader(name, value);
        return this;
    }

    public ItgOk removeHeader(String name) {
        request().removeHeader(name);
        return this;
    }

    public ItgOk headers(Headers headers) {
        request().headers(headers);
        return this;
    }


    public ItgOk cacheControl(CacheControl cacheControl) {
        request().cacheControl(cacheControl);
        return this;
    }


    public ItgOk tag(@Nullable Object tag) {
        request().tag(tag);
        return this;
    }


    public <T> ItgOk tag(Class<? super T> type, @Nullable T tag) {
        request().tag(tag);
        return this;
    }


    public ItgOk method(String method) {
        request().method(method);
        return this;
    }


    public ItgOk autoCancel(Lifecycle lifecycle) {
        request().autoCancel(lifecycle);
        return this;
    }


    public ItgOk addMultiFile(String name, String file) {
        addMultiFile(name, null, null, file);
        return this;
    }


    public ItgOk addMultiFile(String name, File file) {
        addMultiFile(name, null, null, file);
        return this;
    }


    public ItgOk addMultiFile(String name, MediaType mediaType, String fileName, File file) {
        request().addMultiFile(name, mediaType, fileName, file);
        return this;
    }


    public ItgOk addMultiFile(String name, MediaType mediaType, String fileName, String file) {
        request().addMultiFile(name, mediaType, fileName, file);
        return this;
    }


    public ItgOk addMultiParams(String key, String param) {
        request().addMultiFile(key, param);
        return this;
    }


    public ItgOk addMultiJson(String key, Object json) {
        request().addMultiJson(key, json);
        return this;
    }


    public ItgOk addParams(String key, String params) {
        request().addParams(key, params);
        return this;
    }

    public ItgOk addParams(String key, int params) {
        addParams(key, String.valueOf(params));
        return this;
    }

    public ItgOk addParams(String key, double params) {
        addParams(key, String.valueOf(params));
        return this;
    }

    public ItgOk addParams(String key, float params) {
        addParams(key, String.valueOf(params));
        return this;
    }


    public ItgOk addFile(String file) {
        request().addFile(file);
        return this;
    }


    public ItgOk addFile(File file) {
        request().addFile(file);
        return this;
    }


    public ItgOk addJson(Object obj) {
        request().addJson(obj);
        return this;
    }


    public ItgOk addContent(String content) {
        request().addContent(content);
        return this;
    }

    public void go(final Callback callback) {
        Request request = request().build();
        L.e(request.url().toString());
        final Call call = okHttpClient.newCall(request);
        request().autoCancel(new ItgRequest.CancelRequestObserver(call));
        releaseBuild();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getMessage().startsWith("Failed to connect to")) {
                    Toast.makeText(application, "不能连接到网络", Toast.LENGTH_SHORT).show();
                }
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }


    public static class Builder {
        private String globalUrl;
        private boolean openLog;
        private LoggerInterceptor.HL level;
        private Interceptor[] interceptors;
        private Application application;
        private String writePath;

        public Builder globalUrl(String url) {
            if (url != null) {
                char lastChar = url.toCharArray()[url.length() - 1];
                if (lastChar != '/') {
                    globalUrl = url + "/";
                } else {
                    globalUrl = url;
                }
            }
            return this;
        }


        public Builder openHttpLog(boolean open) {
            openLog = open;
            return this;
        }

        public Builder logLevel(LoggerInterceptor.HL hl) {
            level = hl;
            return this;
        }

        public Builder addInterceptors(Interceptor... interceptor) {
            interceptors = interceptor;
            return this;
        }

        public Builder write(String path) {
            this.writePath = path;
            return this;
        }


        public void build(Application application) {
            this.application = application;
            if (itgOk == null) {
                synchronized (ItgOk.class) {
                    if (itgOk == null) {
                        itgOk = new ItgOk(this);
                    }
                }
            }
        }

    }
}
