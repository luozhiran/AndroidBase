package com.plugin.androidbase.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsDetailCssService {
    @GET
    Observable<String> getNewsDetailCss(@Url String url);
}
