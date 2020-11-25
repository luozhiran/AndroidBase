package com.yk.mvvm.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public interface BindingCollectionAdapter<T> {
    /**
     * 为adapter设置ItemBinding
     * @param itemBinding
     */
    void setItemBinding(ItemBinding<T> itemBinding);

    /**
     *返回adapter被设置的ItemBinding
     * @return
     */
    ItemBinding<T> getItemBinding();

    /**
     * 设置adapter的每一项，这些项会以ItemBinding为基础显示，如果你传入的是ObservableList,adapter会监听Observable变化而
     * 改变它自己。
     * 注意adapter会保持传入的ObservableList的直接引用，任何变化必须在主线程中。
     * 另外，如果你不使用ObservableList,你必须使用notifyDataSetChanged()改变。
     * @param items
     */
    void setItems(@Nullable List<T> items);

    /**
     *通过传入的position返回对应的item，这个非常有用，它是一个变体，可以被使用者改写
     * @param position
     * @return
     */
    T getAdapterItem(int position);

    /**
     *调用创建一个binding。它的实现类，通过layoutRes和viewGroup创建一个dataBinding
     * @param inflater
     * @param layoutRes
     * @param viewGroup
     * @return
     */
    ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutRes, ViewGroup viewGroup);


    /**
     *
     * @param binding
     * @param variableId
     * @param layoutRes
     * @param position
     * @param item
     */
    void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item);
}
