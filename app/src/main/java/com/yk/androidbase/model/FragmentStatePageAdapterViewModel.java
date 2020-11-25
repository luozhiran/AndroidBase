package com.yk.androidbase.model;

import com.yk.androidbase.fragments.RecyclerFragment;
import com.yk.androidbase.fragments.SimpleFragment;
import com.yk.androidbase.actions.Outcome0;
import com.yk.androidbase.actions.Outcome1;
import com.yk.androidbase.datas.NewsCategoryData;
import com.yk.androidbase.repositorys.NewsRepository;
import com.yk.mvvm.viewpagerModel.PageStateFragmentModel;

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
