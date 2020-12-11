package com.plugin.widget.bottomSheet;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.plugin.widget.R;

import androidx.annotation.Nullable;

public class SheetBackground extends LinearLayout {
    private float mCornerRadius;
    private int mBackgroundColor;

    public SheetBackground(Context context) {
        super(context);
        init();
    }

    public SheetBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SheetBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int color = getContext().getResources().getColor(R.color.kprogresshud_default_color);
        initBackground(color, mCornerRadius);
    }

    private void initBackground(int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    public void setCornerRadius(float radius) {
        mCornerRadius = (float) (getContext().getResources().getDisplayMetrics().density * radius + 0.5);
        initBackground(mBackgroundColor, mCornerRadius);
    }

    public void setBaseColor(int color) {
        mBackgroundColor = color;
        initBackground(mBackgroundColor, mCornerRadius);
    }
}
