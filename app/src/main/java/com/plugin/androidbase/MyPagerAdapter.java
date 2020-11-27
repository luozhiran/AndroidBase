package com.plugin.androidbase;

import com.plugin.androidbase.fragments.SimpleFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {



    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new SimpleFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
