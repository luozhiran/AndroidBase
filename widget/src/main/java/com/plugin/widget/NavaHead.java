package com.plugin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NavaHead extends FrameLayout {

    private float scale;
    private TextView title;
    private ImageView imageView;

    public NavaHead(@NonNull Context context) {
        this(context, null);
    }

    public NavaHead(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavaHead(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        title = new TextView(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavaHead);
        Drawable drawable = a.getDrawable(R.styleable.NavaHead_src);


        final float textSize = (int) a.getDimension(R.styleable.NavaHead_textSize, 18);
        title.setTextSize(textSize);

        int color = a.getColor(R.styleable.NavaHead_textColor, Color.parseColor("#000000"));
        title.setTextColor(color);

        if (a.hasValue(R.styleable.NavaHead_textBackground)) {
            int background = a.getColor(R.styleable.NavaHead_textBackground, Color.parseColor("#ffffff"));
            title.setBackgroundColor(background);
        }
        String text = a.getString(R.styleable.NavaHead_text);
        title.setText(text);
        if (a.hasValue(R.styleable.NavaHead_textGravity)) {
            int gravity = a.getInt(R.styleable.NavaHead_textGravity, 4);
            title.setGravity(getGravity(gravity));
        } else {
            title.setGravity(Gravity.LEFT);
        }
        int srcWidth = 0;
        int srcHeight = 0;
        if (a.hasValue(R.styleable.NavaHead_srcWidth) && drawable != null) {
            imageView = new ImageView(context);

            srcWidth = (int) a.getDimension(R.styleable.NavaHead_srcWidth, drawable.getIntrinsicWidth());
            srcHeight = (int) a.getDimension(R.styleable.NavaHead_srcHeight, drawable.getIntrinsicHeight());
            LayoutParams layoutParams = new LayoutParams(srcWidth, srcHeight);
            imageView.setPadding(a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingLeft, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingTop, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingRight, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingBottom, 0));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(drawable);
            this.addView(imageView);
        } else if (drawable != null) {
            imageView = new ImageView(context);
            srcWidth = drawable.getIntrinsicWidth();
            srcHeight = drawable.getIntrinsicHeight();
            LayoutParams layoutParams = new LayoutParams(srcWidth, srcHeight);
            imageView.setPadding(a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingLeft, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingTop, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingRight, 0),
                    a.getDimensionPixelOffset(R.styleable.NavaHead_srcPaddingBottom, 0));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(drawable);
            this.addView(imageView);
        }


        this.addView(title);
        a.recycle();
        scale = getResources().getDisplayMetrics().density;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (imageView != null) {
            LayoutParams titleLayout = new LayoutParams(MeasureSpec.getSize(widthMeasureSpec) - imageView.getMeasuredHeight() * 2, LayoutParams.WRAP_CONTENT);
            titleLayout.gravity = Gravity.CENTER;
            title.setLayoutParams(titleLayout);
        }
    }


    public void setSrcOnClick(OnClickListener onClick) {
        if (imageView != null) {
            imageView.setOnClickListener(onClick);
        }
    }

    public void setText(String text) {
        title.setText(text);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }


    public int getGravity(int g) {
        switch (g) {
            case 1:
                return Gravity.CENTER;
            case 2:
                return Gravity.CENTER_VERTICAL;
            case 3:
                return Gravity.CENTER_HORIZONTAL;
            case 4:
                return Gravity.LEFT;
            case 5:
                return Gravity.RIGHT;
            case 6:
                return Gravity.TOP;
            case 7:
                return Gravity.BOTTOM;
        }
        return Gravity.LEFT;
    }
}
