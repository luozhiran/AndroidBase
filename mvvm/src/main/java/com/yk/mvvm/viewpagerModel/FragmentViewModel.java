package com.yk.mvvm.viewpagerModel;

import com.yk.mvvm.base.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public abstract class FragmentViewModel extends ViewModel {
    public final ObservableInt fragmentSize = new ObservableInt();
    public final List<String> titles = new ArrayList<>();


    public void setFragmentSize(int size) {
        fragmentSize.set(size);
    }


    public void insertTitle(String... title) {
        if (title != null && title.length > 0) {
            titles.addAll(Arrays.asList(title));
        }
    }

    public abstract void changeViewModel();
}
