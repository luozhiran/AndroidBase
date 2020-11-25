package com.yk.mvvm.viewpagerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;

public class PageFragmentModel extends FragmentViewModel {
    public List<Fragment> fragments = new ArrayList<>();


    public void insertFragment(Fragment... fragment) {
        if (fragment != null && fragment.length > 0) {
            fragments.addAll(Arrays.asList(fragment));
            setFragmentSize(fragment.length);
        }
    }


    public void insertFragment(List<Fragment> list) {
        if (list != null && list.size() > 0) {
            fragments.addAll(list);
            setFragmentSize(list.size());
        }
    }

    @Override
    public void changeViewModel() {
        if (fragments.size() != 0) {
            setFragmentSize(fragments.size());
        }
    }


}
