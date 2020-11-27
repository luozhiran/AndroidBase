package com.plugin.mvvm.recyclerModel;

import com.plugin.mvvm.MergeObservableList;
import com.plugin.mvvm.base.ViewModel;

import java.util.List;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

/**
 *    <androidx.recyclerview.widget.RecyclerView
 *             android:layout_width="match_parent"
 *             android:layout_height="match_parent"
 *             app:adapter="@{viewModel.adapter}"
 *             app:itemBinding="@{viewModel.multipleItems}"
 *             app:items="@{viewModel.headerFooterItems}"
 *             app:layoutManager="@{LayoutManagers.linear()}"
 *             app:viewHolder="@{viewModel.viewHolder}" />
 * @param <T>
 */
public class RecyclerModel<T> extends ViewModel {

    public final ObservableList<T> items = new ObservableArrayList<>();
    public final ObservableList<Object> header = new ObservableArrayList<>();
    public final ObservableList<Object> footer = new ObservableArrayList<>();

    public final MergeObservableList<Object> headerFooterItems = new MergeObservableList<>()
            .insertList(header)
            .insertList(items)
            .insertList(footer);



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
