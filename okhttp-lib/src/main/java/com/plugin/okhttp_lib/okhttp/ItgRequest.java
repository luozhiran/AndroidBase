package com.plugin.okhttp_lib.okhttp;


import com.itg.lib_log.L;
import com.plugin.okhttp_lib.okhttp.operate.ItgBuilder;

import java.io.File;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;

class ItgRequest extends ItgBuilder {

    private Request.Builder builder;

    private String DEFAULT_METHOD = Ok.GET;

    private Lifecycle lifecycle;

    public static ItgRequest of() {
        return new ItgRequest();
    }


    private ItgRequest() {
        builder = new Request.Builder();
    }

    @Override
    public ItgBuilder url(String url) {
        if (url == null || url.length() == 0) throw new IllegalArgumentException("url isn't null");
        if (url.startsWith("http") || url.startsWith("https")) {
            builder.url(url);
        } else {
            if (url.toCharArray()[0] == '/') {
                url = ItgOk.instance().url + url.replaceFirst("/", "");
            } else {
                url = ItgOk.instance().url  + url;
            }
        }
        builder.url(url);
        return this;
    }

    @Override
    public ItgBuilder header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    @Override
    public ItgBuilder addHeader(String name, String value) {
        builder.addHeader(name, value);
        return this;
    }

    @Override
    public ItgBuilder removeHeader(String name) {
        builder.removeHeader(name);
        return this;
    }

    @Override
    public ItgBuilder headers(Headers headers) {
        builder.headers(headers);
        return this;
    }

    @Override
    public ItgBuilder cacheControl(CacheControl cacheControl) {
        builder.cacheControl(cacheControl);
        return this;
    }

    @Override
    public ItgBuilder method(String method) {
        DEFAULT_METHOD = method;
        return this;
    }


    @Override
    public ItgBuilder tag(@Nullable Object tag) {
        builder.tag(tag);
        return this;
    }

    @Override
    public <T> ItgBuilder tag(Class<? super T> type, @Nullable T tag) {
        builder.tag(type, tag);
        return this;
    }


    @Override
    public ItgBuilder autoCancel(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        return this;
    }


    protected Request build() {
        execParams();
        return builder.build();
    }


    private void execParams() {
        switch (DEFAULT_METHOD) {
            case Ok.GET:
                getSingleBuilder().build(builder, Ok.GET);
                break;
            case Ok.POST:
                if (isMulti()) {
                    getMultiBuilder().build(builder);
                } else {
                    getSingleBuilder().build(builder, Ok.POST);
                }
                break;
            case Ok.PATCH:
                break;
            case Ok.PUT:
                break;
            case Ok.DELETE:
                break;
            case Ok.MOVE:
                break;
        }

    }


    @Override
    public ItgBuilder addMultiFile(String name, String file) {
        getMultiBuilder().addMultiFile(name, file);
        return this;
    }

    @Override
    public ItgBuilder addMultiFile(String name, File file) {
        getMultiBuilder().addMultiFile(name, file);
        return this;
    }

    @Override
    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, File file) {
        getMultiBuilder().addMultiFile(name, mediaType, fileName, file);
        return this;
    }

    @Override
    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, String file) {
        getMultiBuilder().addMultiFile(name, mediaType, fileName, file);
        return this;
    }

    @Override
    public ItgBuilder addMultiParams(String key, String param) {
        getMultiBuilder().addMultiParams(key, param);
        return this;
    }

    @Override
    public ItgBuilder addMultiJson(String key, Object json) {
        getMultiBuilder().addMultiJson(key, json);
        return this;
    }


    public ItgBuilder addMultiParams(String key, int param) {
        addMultiParams(key, String.valueOf(param));
        return this;
    }

    public ItgBuilder addMultiParams(String key, double param) {
        addMultiParams(key, String.valueOf(param));
        return this;
    }

    public ItgBuilder addMultiParams(String key, float param) {
        addMultiParams(key, String.valueOf(param));
        return this;
    }

    @Override
    public ItgBuilder addParams(String key, String params) {
        getSingleBuilder().addParams(key, params);
        return this;
    }

    public ItgBuilder addParams(String key, int params) {
        addParams(key, String.valueOf(params));
        return this;
    }

    public ItgBuilder addParams(String key, double params) {
        addParams(key, String.valueOf(params));
        return this;
    }

    public ItgBuilder addParams(String key, float params) {
        addParams(key, String.valueOf(params));
        return this;
    }

    @Override
    public ItgBuilder addFile(String file) {
        getSingleBuilder().addFile(file);
        return this;
    }

    @Override
    public ItgBuilder addFile(File file) {
        getSingleBuilder().addFile(file);
        return this;
    }

    @Override
    public ItgBuilder addJson(Object obj) {
        getSingleBuilder().addJson(obj);
        return this;
    }

    @Override
    public ItgBuilder addContent(String content) {
        getSingleBuilder().addContent(content);
        return this;
    }

    public void autoCancel(CancelRequestObserver observer) {
        if (lifecycle != null) {
            lifecycle.addObserver(observer);
        }
    }

    public static class CancelRequestObserver implements LifecycleEventObserver {

        private Call call;

        public CancelRequestObserver(Call call) {
            this.call = call;
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                call.cancel();
                L.e("取消---------------------------------");
            }
        }
    }
}
