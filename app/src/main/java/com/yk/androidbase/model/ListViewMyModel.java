package com.yk.androidbase.model;

import com.yk.androidbase.BR;
import com.yk.androidbase.R;
import com.yk.androidbase.datas.NewsData;
import com.yk.mvvm.common.OnItemBindClass;
import com.yk.mvvm.listviewModel.ListViewModel;



public class ListViewMyModel extends ListViewModel<NewsData> {

    public final OnItemBindClass<Object> multipleItems = new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.item_header_footer)
            .map(NewsData.class, BR.item, R.layout.news_item);

}
