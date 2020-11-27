package com.plugin.androidbase.api;

import com.plugin.androidbase.datas.NewsCategoryData;
import com.plugin.androidbase.datas.NewsData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsCategoryApi {
    @GET("/category")
    Call<List<NewsCategoryData>> getNewsCategory();

    @GET("/{path}")
    Call<List<NewsData>> getCommentNews(@Path("path")String path);
}
