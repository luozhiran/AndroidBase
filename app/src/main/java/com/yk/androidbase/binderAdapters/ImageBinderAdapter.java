package com.yk.androidbase.binderAdapters;

import android.widget.ImageView;
import android.widget.TextView;

import com.itg.lib_log.L;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

public class ImageBinderAdapter {

    @BindingAdapter("url")
    public static void setImgUrl(ImageView imageView, int url) {

        imageView.setImageResource(url);
    }

    @BindingAdapter("items")
    public static void changeList(TextView textView, List<Integer> items){

    }
}
