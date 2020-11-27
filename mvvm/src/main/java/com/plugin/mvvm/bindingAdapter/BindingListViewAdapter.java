package com.plugin.mvvm.bindingAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.plugin.itg_util.Utils;
import com.plugin.mvvm.common.BindingCollectionAdapter;
import com.plugin.mvvm.common.ItemBinding;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

public class BindingListViewAdapter<T> extends BaseAdapter implements BindingCollectionAdapter<T> {

    private LayoutInflater inflater;
    private List<T> items;
    private int[] layouts;
    private ItemBinding<T> itemBinding;
    private final int itemTypeCount;
    private ItemIds<? super T> itemIds;
    private ItemIsEnabled<? super T> itemIsEnabled;
    private final WeakReferenceOnListChangedCallback<T> callback = new WeakReferenceOnListChangedCallback<>(this);
    public BindingListViewAdapter(int itemTypeCount) {
        this.itemTypeCount = itemTypeCount;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemIds == null ? position : itemIds.getItemId(position, items.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        int viewType = getItemViewType(position);
        //同过类型，获取布局
        int layoutRes = layouts[viewType];

        ViewDataBinding binding;
        if (convertView == null) {
            binding = onCreateBinding(inflater, layoutRes, parent);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }

        T item = items.get(position);
        onBindBinding(binding, itemBinding.variableId(), layoutRes, position, item);
        return binding.getRoot();
    }


    @Override
    public void setItemBinding(ItemBinding<T> itemBinding) {
        this.itemBinding = itemBinding;
    }

    @Override
    public ItemBinding<T> getItemBinding() {
        return itemBinding;
    }

    @Override
    public void setItems(@Nullable List<T> items) {
        if (this.items == items) {
            return;
        }
        if (this.items instanceof ObservableList) {
            ((ObservableList<T>) this.items).removeOnListChangedCallback(callback);
        }
        if (items instanceof ObservableList) {
            ((ObservableList<T>) items).addOnListChangedCallback(callback);
        }
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public T getAdapterItem(int position) {
        return null;
    }

    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, int layoutRes, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater,layoutRes,viewGroup,false);
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int variableId, int layoutRes, int position, T item) {
        if (itemBinding.bind(binding, item)) {
            binding.executePendingBindings();
        }
    }

    @Override
    public boolean hasStableIds() {
         return itemIds != null;
    }

    public void setItemIds(@Nullable ItemIds<? super T> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public int getViewTypeCount() {
        return ensureLayoutsInit();
    }

    private int ensureLayoutsInit() {
        int count = itemTypeCount;
        if (layouts == null) {
            layouts = new int[count];
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        ensureLayoutsInit();
        T item = items.get(position);
        itemBinding.onItemBind(position,item);
        int firstEmpty = 0;
        for (int i = 0; i < layouts.length; i++) {
            if (itemBinding.layoutRes() == layouts[i]) {
                return i;
            }
            if (layouts[i] == 0) {
                firstEmpty = i;
            }
        }
        layouts[firstEmpty] = itemBinding.layoutRes();
        return firstEmpty;
    }


    public void setItemIsEnabled(@Nullable ItemIsEnabled<? super T> itemIsEnabled) {
        this.itemIsEnabled = itemIsEnabled;
    }

    @Override
    public boolean isEnabled(int position) {
        return itemIsEnabled == null || itemIsEnabled.isEnabled(position, items.get(position));
    }
    public interface ItemIds<T> {
        long getItemId(int position, T item);
    }

    public interface ItemIsEnabled<T> {
        boolean isEnabled(int position, T item);
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
        final WeakReference<BindingListViewAdapter<T>> adapterRef;

        WeakReferenceOnListChangedCallback(BindingListViewAdapter<T> adapter) {
            this.adapterRef = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            BindingListViewAdapter<T> adapter = adapterRef.get();
            if (adapter == null) {
                return;
            }
            Utils.ensureChangeOnMainThread();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            onChanged(sender);
        }
    }
}
