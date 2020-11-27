package com.plugin.androidbase.model;

import com.plugin.androidbase.repositorys.NewsRepository;
import com.plugin.mvvm.viewpagerModel.PageFragmentModel;

public class FragmentPageAdapterViewModel extends PageFragmentModel {

    private NewsRepository newsRepository;

    public FragmentPageAdapterViewModel() {
        newsRepository = new NewsRepository();
    }


}
