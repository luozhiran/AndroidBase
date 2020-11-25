package com.yk.mvvm;

import androidx.annotation.LayoutRes;

/**
 * 提供recyclerview viewPager ListView 的布局文件
 */
public class ItemView {
    @LayoutRes
    private int layoutRes;

    public static ItemView of(@LayoutRes int layoutRes) {
        return new ItemView().setLayoutRes(layoutRes);

    }

    public ItemView setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }
}
