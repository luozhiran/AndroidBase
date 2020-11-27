package com.plugin.mvvm.common;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;

public class OnItemBindClass<T> implements OnItemBind<T> {


    private final SimpleArrayMap<Class<? extends T>, int[]> itemBindingMap;

    public OnItemBindClass() {
        this.itemBindingMap = new SimpleArrayMap<>();
    }

    public OnItemBindClass<T> map(@NonNull Class<? extends T> itemClass, int variableId, @LayoutRes int layoutRes) {
        itemBindingMap.put(itemClass, new int[]{variableId, layoutRes});
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
                return;
            }
        }
        throw new IllegalArgumentException("Missing class for item " + item);
    }
}
