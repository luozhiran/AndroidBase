package com.plugin.androidbase.binderAdapters;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.databinding.BindingAdapter;

public class ImageBinderAdapter {

    @BindingAdapter("url")
    public static void setImgUrl(ImageView imageView, int url) {

        imageView.setImageResource(url);
    }

    @BindingAdapter("items")
    public static void changeList(TextView textView, List<Integer> items){

    }
}
