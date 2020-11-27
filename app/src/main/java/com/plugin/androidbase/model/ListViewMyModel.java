package com.plugin.androidbase.model;

import com.plugin.androidbase.BR;
import com.plugin.androidbase.R;
import com.plugin.androidbase.datas.NewsData;
import com.plugin.mvvm.common.OnItemBindClass;
import com.plugin.mvvm.listviewModel.ListViewModel;



public class ListViewMyModel extends ListViewModel<NewsData> {

    public final OnItemBindClass<Object> multipleItems = new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.item_header_footer)
            .map(NewsData.class, BR.item, R.layout.news_item);

}
