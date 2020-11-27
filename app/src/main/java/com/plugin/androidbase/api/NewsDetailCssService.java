package com.plugin.androidbase.api;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface NewsDetailCssService {
    @GET
    Observable<String> getNewsDetailCss(@Url String url);
}
