package com.plugin.mvvm.common;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;

public class OnItemBindClass<T> implements OnItemBind<T> {


    private final SimpleArrayMap<Class<? extends T>, int[]> itemBindingMap;
    private SparseArray<Object> extraBindings;

    public OnItemBindClass() {
        this.itemBindingMap = new SimpleArrayMap<>();
    }

    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, int variableId, @LayoutRes int layoutRes) {
        itemBindingMap.put(itemClass, new int[]{variableId, layoutRes});
        return this;
    }

    public final OnItemBindClass<T> bindExtra(int variableId, Object value) {
        if (extraBindings == null) {
            extraBindings = new SparseArray<>(1);
        }
        extraBindings.put(variableId, value);
        return this;
    }

    public int itemTypeCount() {
        return itemBindingMap.size();
    }

    @Override
    public void onItemBind(ItemBinding itemBinding, int position, T item) {
        for (int i = 0; i < itemBindingMap.size(); i++) {
            Class<? extends T> key = itemBindingMap.keyAt(i);
            if (key.isInstance(item)) {
                int[] values = itemBindingMap.valueAt(i);
                itemBinding.set(values[0], values[1]);
                if (extraBindings != null) {
                    itemBinding.bindExtra(extraBindings);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Missing class for item (定义的实体或头实体和传输的实体类型不匹配)" + item);
    }
}
