package com.plugin.androidbase.repositorys;

import com.itg.lib_log.L;
import com.plugin.androidbase.actions.Outcome0;
import com.plugin.androidbase.actions.Outcome1;

import com.plugin.androidbase.api.NewsCategoryApi;
import com.plugin.androidbase.datas.NewsCategoryData;

import com.plugin.androidbase.datas.NewsData;
import com.plugin.mvvm.base.DataModel;
import com.plugin.okhttp_lib.retrofit.RfOk;
import com.plugin.threadtool.ThreadClient;


import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;


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
