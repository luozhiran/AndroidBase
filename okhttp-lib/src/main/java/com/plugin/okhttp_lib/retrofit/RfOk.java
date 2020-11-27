package com.plugin.okhttp_lib.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plugin.okhttp_lib.okhttp.ItgOk;
import com.plugin.okhttp_lib.retrofit.gson.DoubleDefaultAdapter;
import com.plugin.okhttp_lib.retrofit.gson.IntegerDefaultAdapter;
import com.plugin.okhttp_lib.retrofit.gson.LongDefaultAdapter;
import com.plugin.okhttp_lib.retrofit.gson.StringNullAdapter;

import java.lang.reflect.Field;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RfOk {
    private static volatile RfOk rfOk = null;
    private Retrofit retrofit;

    public static RfOk instance() {
        if (rfOk == null) {
            synchronized (RfOk.class) {
                if (rfOk == null) {
                    rfOk = new RfOk();
                }
            }
        }
        return rfOk;
    }


    public RfOk() {
        Retrofit.Builder builder = new Retrofit.Builder();
        //后期自定义数据转换器，把GSON换成JSON
        Gson gson = new GsonBuilder().
                registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                .registerTypeAdapter(long.class, new LongDefaultAdapter())
                .registerTypeAdapter(String.class, new StringNullAdapter())
                .create();
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        ItgOk ok = ItgOk.instance();
        try {
            Field field = ok.getClass().getDeclaredField("okHttpClient");
            field.setAccessible(true);
            OkHttpClient okHttpClient = (OkHttpClient) field.get(ItgOk.instance());
            assert okHttpClient != null;
            builder.client(okHttpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.baseUrl(ItgOk.GLOBAL_URL);
        retrofit = builder.build();

    }


    public <T> T create(Class<T> tClass){
        return retrofit.create(tClass);
    }

}
