package com.plugin.mvvm.listviewModel;

import com.plugin.mvvm.MergeObservableList;
import com.plugin.mvvm.base.ViewModel;
import com.plugin.mvvm.bindingAdapter.BindingListViewAdapter;

import java.util.List;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

/**
 * <ListView
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * app:itemBinding="@{viewModel.multipleItems}"
 * app:itemIds="@{viewModel.itemIds}"
 * app:itemTypeCount="@{viewModel.multipleItems.itemTypeCount}"
 * app:items="@{viewModel.headerFooterItems}" />
 *
 * @param <T>
 */

public class ListViewModel<T> extends ViewModel {

    public final ObservableList<T> items = new ObservableArrayList<>();
    public final ObservableList<Object> header = new ObservableArrayList<>();
    public final ObservableList<Object> footer = new ObservableArrayList<>();

    public final MergeObservableList<Object> headerFooterItems = new MergeObservableList<>()
            .insertList(header)
            .insertList(items)
            .insertList(footer);

    public final BindingListViewAdapter.ItemIds<Object> itemIds = new BindingListViewAdapter.ItemIds<Object>() {
        @Override
        public long getItemId(int position, Object item) {
            return position;
        }
    };


    public void insertItem(T item) {
        items.add(item);
    }

    public void insertItemList(List<T> item) {
        if (item != null) {
            items.addAll(item);
        }
    }

    public void insertHeader(Object header) {
        this.header.add(header);
    }

    public void insertFooter(Object footer) {
        this.footer.add(footer);
    }


}
