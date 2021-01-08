package com.plugin.mvvm.bindingAdapter;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ItgFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments;
    private List<String> pageTitle;

    private ItgFragmentPagerAdapter(@NonNull FragmentManager fm, List<Fragment> list, List<String> pageTitle) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        if (list != null && list.size() > 0) {
            fragments = new ArrayList<>();
            fragments.addAll(list);
        }
        this.pageTitle = pageTitle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    public static ItgFragmentPagerAdapter of(FragmentManager fm, List<Fragment> list, List<String> pageTitle) {
        ItgFragmentPagerAdapter itgFragmentPagerAdapter = new ItgFragmentPagerAdapter(fm, list, pageTitle);
        return itgFragmentPagerAdapter;
    }


}
