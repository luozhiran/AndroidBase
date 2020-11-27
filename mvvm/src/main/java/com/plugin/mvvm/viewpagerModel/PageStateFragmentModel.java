package com.plugin.mvvm.viewpagerModel;

import androidx.fragment.app.Fragment;

public abstract class PageStateFragmentModel extends FragmentViewModel {

    public FragmentFactory fragmentFactory;


    public PageStateFragmentModel() {
        fragmentFactory = initFragmentFactory();
        if (fragmentFactory == null)
            throw new IllegalArgumentException("请复写 initFragmentFactory()");
    }

    public abstract FragmentFactory initFragmentFactory();

    public interface FragmentFactory {
        Fragment create(int position);
    }


    @Override
    public void changeViewModel() {
        if (titles.size() > 0) {
            setFragmentSize(titles.size());
        }
    }
}
