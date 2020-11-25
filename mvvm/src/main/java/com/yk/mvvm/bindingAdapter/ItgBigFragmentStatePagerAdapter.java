package com.yk.mvvm.bindingAdapter;


import com.yk.mvvm.viewpagerModel.PageStateFragmentModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 *
 */
public class ItgBigFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private PageStateFragmentModel.FragmentFactory factory;
    private int count = 0;
    private List<String> pageTitle;

    private ItgBigFragmentStatePagerAdapter(@NonNull FragmentManager fm, PageStateFragmentModel.FragmentFactory factory, int count,List<String> pageTitle) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.factory = factory;
        this.count = count;
        this.pageTitle = pageTitle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return factory.create(position);
    }

    @Override
    public int getCount() {
        return count;
    }


    public static ItgBigFragmentStatePagerAdapter of(FragmentManager fm, PageStateFragmentModel.FragmentFactory factory, int count,List<String> pageTitle) {
        return new ItgBigFragmentStatePagerAdapter(fm, factory, count,pageTitle);
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (pageTitle != null && pageTitle.size() > position) {
            return pageTitle.get(position);
        } else {
            return super.getPageTitle(position);
        }

    }
}
