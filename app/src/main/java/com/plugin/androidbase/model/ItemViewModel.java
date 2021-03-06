package com.plugin.androidbase.model;

import android.view.View;

import com.plugin.androidbase.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ItemViewModel extends BaseObservable {
    public final boolean checkable;
    @Bindable
    private int index;
    @Bindable
    private boolean checked;

    public ItemViewModel(int index, boolean checkable) {
        this.index = index;
        this.checkable = checkable;
    }

    public int getIndex() {
        return index;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean onToggleChecked(View v) {
        if (!checkable) {
            return false;
        }
        checked = !checked;
        notifyPropertyChanged(BR.checked);
        return true;
    }
}
