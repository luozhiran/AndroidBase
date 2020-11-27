package com.plugin.mvvm.common;


/**
 *为集合中item设置回调
 * @param <T>
 */
public interface OnItemBind<T> {
    void onItemBind(ItemBinding itemBinding, int position, T item);
}
