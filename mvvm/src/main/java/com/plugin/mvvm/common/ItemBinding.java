package com.plugin.mvvm.common;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

/**
 * 为集合中绑定在一起item和view，提供必要的信息。信息包含variable id(编译器自动生成的)，和layout文件，
 * 以及一些其他的信息
 *
 * @param <T>
 */
public class ItemBinding<T> {

    /**
     * 使用下面的常量，表示variable id，如果不需要数据，项header和footer，不需要把item和layout绑定在一起
     */
    public static final int VAR_NONE = 0;
    private static final int VAR_INVALID = -1;

    /**
     * Constructs an instance with the given variable id and layout.
     */
    public static <T> ItemBinding<T> of(int variableId, @LayoutRes int layoutRes) {
        return new ItemBinding<T>(null).set(variableId, layoutRes);
    }

    public static <T> ItemBinding<T> of(OnItemBind<T> onItemBind) {
        if (onItemBind == null) {
            throw new NullPointerException("onItemBind == null");
        }
        return new ItemBinding<>(onItemBind);
    }


    private final OnItemBind<T> onItemBind;
    private int variableId;
    @LayoutRes
    private int layoutRes;
    private SparseArray<Object> extraBindings;


    private ItemBinding(OnItemBind<T> onItemBind) {
        this.onItemBind = onItemBind;
    }

    public final ItemBinding<T> set(int variableId, @LayoutRes int layoutRes) {
        this.variableId = variableId;
        this.layoutRes = layoutRes;
        return this;
    }


    public final ItemBinding<T> variableId(int variableId) {
        this.variableId = variableId;
        return this;
    }

    public final ItemBinding<T> layoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return this;
    }

    public final ItemBinding<T> bindExtra(int variableId, Object value) {
        if (extraBindings == null) {
            extraBindings = new SparseArray<>(1);
        }
        extraBindings.put(variableId, value);
        return this;
    }


    public final ItemBinding<T> bindExtra(SparseArray<Object> sparseArray) {
        extraBindings = sparseArray;
        return this;
    }

    public final int variableId() {
        return variableId;
    }

    public final int layoutRes() {
        return layoutRes;
    }

    public final Object extraBinding(int variableId) {
        if (extraBindings == null) {
            return null;
        }
        return extraBindings.get(variableId);
    }

    public void onItemBind(int position, T item) {
        if (onItemBind != null) {
            variableId = VAR_INVALID;
            layoutRes = 0;
            onItemBind.onItemBind(this, position, item);
            if (variableId == VAR_INVALID) {
                throw new IllegalStateException("variableId not set in onItemBind()");
            }
            if (layoutRes == 0) {
                throw new IllegalStateException("layoutRes not set in onItemBind()");
            }
        }
    }

    public boolean bind(ViewDataBinding binding, T item) {
        if (variableId == VAR_NONE) {
            return false;
        }
        boolean result = binding.setVariable(variableId, item);
        if (!result) {
            ErrorUtils.throwMissingVariable(binding, variableId, layoutRes);
        }
        if (extraBindings != null) {
            for (int i = 0, size = extraBindings.size(); i < size; i++) {
                int variableId = extraBindings.keyAt(i);
                Object value = extraBindings.valueAt(i);
                if (variableId != VAR_NONE) {
                    binding.setVariable(variableId, value);
                }
            }
        }
        return true;
    }
}
