package com.plugin.widget.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class BottomBar extends ViewGroup {
    public BottomBar(Context context) {
        this(context,null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
