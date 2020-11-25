package com.yk.androidbase.model;

import com.yk.androidbase.repositorys.NewsRepository;
import com.yk.mvvm.viewpagerModel.PageFragmentModel;

public class FragmentPageAdapterViewModel extends PageFragmentModel {

    private NewsRepository newsRepository;

    public FragmentPageAdapterViewModel() {
        newsRepository = new NewsRepository();
    }


}
