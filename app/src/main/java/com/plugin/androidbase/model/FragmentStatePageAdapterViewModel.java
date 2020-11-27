package com.plugin.androidbase.model;

import com.plugin.androidbase.fragments.RecyclerFragment;
import com.plugin.androidbase.fragments.SimpleFragment;
import com.plugin.androidbase.actions.Outcome0;
import com.plugin.androidbase.actions.Outcome1;
import com.plugin.androidbase.datas.NewsCategoryData;
import com.plugin.androidbase.repositorys.NewsRepository;
import com.plugin.mvvm.viewpagerModel.PageStateFragmentModel;

import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;

public class FragmentStatePageAdapterViewModel extends PageStateFragmentModel {


    private NewsRepository newsRepository;

    public FragmentStatePageAdapterViewModel() {
        newsRepository = new NewsRepository();
    }

    @Override
    public FragmentFactory initFragmentFactory() {
        return new FragmentFactory() {
            @Override
            public Fragment create(int position) {
                if (position == fragmentSize.get() - 1) {
                    return RecyclerFragment.instance();
                } else {
                    return SimpleFragment.newInstance(position);
                }

            }
        };
    }

    public void getNewsCategory() {
        newsRepository.loadNewsCategory(new Outcome0<List<NewsCategoryData>>() {
            @Override
            public void call(List<NewsCategoryData> newsCategoryData) {
                for (NewsCategoryData data : newsCategoryData) {
                    insertTitle(data.getName());
                }
                changeViewModel();

            }
        }, new Outcome1<String>() {
            @Override
            public void error(Call call, String s) {

            }
        });

    }


}
