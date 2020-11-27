package com.plugin.androidbase.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itg.lib_log.L;
import com.plugin.androidbase.BR;
import com.plugin.androidbase.R;
import com.plugin.androidbase.datas.NewsData;
import com.plugin.mvvm.bindingAdapter.BindingRecyclerViewAdapter;
import com.plugin.mvvm.common.OnItemBindClass;
import com.plugin.mvvm.recyclerModel.RecyclerModel;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMyModel extends RecyclerModel<NewsData> {

    public final LoggingRecyclerViewAdapter<Object> adapter = new LoggingRecyclerViewAdapter<>();

    public final OnItemBindClass<Object> multipleItems = new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.item_header_footer)
            .map(NewsData.class, BR.item, R.layout.news_item);



    public final BindingRecyclerViewAdapter.ViewHolderFactory viewHolder = new BindingRecyclerViewAdapter.ViewHolderFactory() {
        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
            return new MyAwesomeViewHolder(binding.getRoot());
        }
    };


    private static class MyAwesomeViewHolder extends RecyclerView.ViewHolder {
        public MyAwesomeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class LoggingRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T>{

        @Override
        public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
            ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
            L.e(binding+"");
            return binding;
        }

        @Override
        public void onBindBinding(ViewDataBinding binding, int variableId, @LayoutRes int layoutRes, int position, T item) {
            super.onBindBinding(binding, variableId, layoutRes, position, item);
            L.e("bound binding: " + binding + " at position: " + position);
        }
    }
}
