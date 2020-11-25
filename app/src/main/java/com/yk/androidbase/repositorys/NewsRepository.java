package com.yk.androidbase.repositorys;

import android.content.Context;

import com.itg.lib_log.L;
import com.yk.androidbase.actions.Outcome0;
import com.yk.androidbase.actions.Outcome1;
import com.yk.androidbase.converter.GsonConverterFactory;

import com.yk.androidbase.api.NewsCategoryApi;
import com.yk.androidbase.datas.NewsCategoryData;

import com.yk.androidbase.datas.NewsData;
import com.yk.mvvm.base.DataModel;
import com.yk.okhttp_lib.retrofit.RfOk;
import com.yk.threadtool.ThreadClient;


import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Model是什么呢？其实就是数据源，可以简单理解是我们用JSON转过来的BeanViewModel要把数据映射到UI中可能需要大量对Model的数据拷贝和操作，
 * 拿Model的字段去生成对应的ObservableField然后绑定到UI（我们不会直接拿Model的数据去做绑定展示），这里是有必要在一个ViewModel保留原始的Model引用，
 * 这对于我们是非常有用的，因为可能用户的某些操作和输入需要我们去改变数据源，可能我们需要把一个Bean在列表页点击后传给详情页，
 * 可能我们需要把这个Model当做表单提交到服务器。这些都需要我们的ViewModel持有相应的Model（数据源）。
 */
public class NewsRepository implements DataModel {

    private ThreadClient threadClient = new ThreadClient();

    public void loadNewsCategory(Outcome0<List<NewsCategoryData>> outcome0, Outcome1<String> outcome1) {
        RfOk.instance().create(NewsCategoryApi.class).getNewsCategory()
                .enqueue(new Callback<List<NewsCategoryData>>() {
                    @Override
                    public void onResponse(Call<List<NewsCategoryData>> call, Response<List<NewsCategoryData>> response) {
                        if (response.code() == 200) {
                            outcome0.call(response.body());
                        } else {
                            L.e(response.body() + "===========");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<NewsCategoryData>> call, Throwable t) {
                        L.e(t.getMessage());
                        outcome1.error(call,t.getMessage());
                    }
                });
    }

    public void loadCommentNews(String path,Outcome0<List<NewsData>> outcome0, Outcome1<String> outcome1){
        RfOk.instance()
                .create(NewsCategoryApi.class)
                .getCommentNews(path)
                .enqueue(new Callback<List<NewsData>>() {
                    @Override
                    public void onResponse(Call<List<NewsData>> call, Response<List<NewsData>> response) {
                        if (response.code() == 200) {
                            outcome0.call(response.body());
                        } else {
                            L.e(response.body() + "===========");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NewsData>> call, Throwable t) {
                        L.e(t.getMessage());
                        outcome1.error(call,t.getMessage());
                    }
                });
    }


}
