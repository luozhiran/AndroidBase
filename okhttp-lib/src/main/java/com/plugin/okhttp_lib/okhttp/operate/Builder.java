package com.plugin.okhttp_lib.okhttp.operate;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import okhttp3.CacheControl;
import okhttp3.Headers;

public interface Builder {

    ItgBuilder url(String url);

    ItgBuilder header(String name, String value);

    ItgBuilder addHeader(String name, String value);

    ItgBuilder removeHeader(String name);

    ItgBuilder headers(Headers headers);

    ItgBuilder cacheControl(CacheControl cacheControl);

    ItgBuilder tag(@Nullable Object tag);

    <T> ItgBuilder tag(Class<? super T> type, @Nullable T tag);

    ItgBuilder method(String method);

    ItgBuilder autoCancel(Lifecycle lifecycle);


}
