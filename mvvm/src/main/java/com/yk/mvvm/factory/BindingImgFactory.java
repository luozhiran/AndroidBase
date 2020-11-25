package com.yk.mvvm.factory;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;

public class BindingImgFactory {
    @BindingAdapter("netImg")
    public static void bindNetImg(ImageView img, String url) {
        Glide.with(img.getContext()).load(url).into(img);
    }
}
