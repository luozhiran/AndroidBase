package com.yk.androidbase.binderAdapters;

import android.widget.ImageView;

import com.itg.lib_log.L;
import com.yk.androidbase.datas.NewsCategoryData;
import java.util.List;
import androidx.databinding.BindingAdapter;


public class TestBinderAdapter {

    @BindingAdapter("items")
    public static void setItems(ImageView tabLayout, List<NewsCategoryData> a){

        if (a == null){
            L.e("category数据改变------null---");
        }else {
            L.e("category数据改变---------"+a.size());
        }

    }


}
