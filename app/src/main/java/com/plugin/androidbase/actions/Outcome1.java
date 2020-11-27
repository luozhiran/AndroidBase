package com.plugin.androidbase.actions;


import retrofit2.Call;

public interface Outcome1<T> {
    void error(Call call, T t);
}
