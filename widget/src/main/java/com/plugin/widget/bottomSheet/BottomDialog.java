package com.plugin.widget.bottomSheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.plugin.widget.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class BottomDialog {

    private float mDimAmount;
    private int mGraceTimeMs;
    private SheetDialog mBottomDialog;
    private boolean mFinished;
    private Handler mGraceTimer;
    private Context mContext;
    private int mWindowColor;
    private float mCornerRadius;
    private List<Content> mContents;

    public enum Style {
        VERTICAL
    }

    private BottomDialog(Context context) {
        this.mContext = context;
        mBottomDialog = new SheetDialog(context);
        mDimAmount = 0;
        mWindowColor = context.getResources().getColor(R.color.kprogresshud_default_color);
        mCornerRadius = 10;
        mFinished = false;
        mGraceTimeMs = 0;
    }


    public BottomDialog setWindowColor(int color) {
        mWindowColor = color;
        return this;
    }


    public BottomDialog setBackgroundColor(int color) {
        mWindowColor = color;
        return this;
    }

    public BottomDialog setGraceTime(int graceTimeMs) {
        mGraceTimeMs = graceTimeMs;
        return this;
    }

    public BottomDialog show() {
        if (!isShowing()) {
            mFinished = false;
            if (mGraceTimeMs == 0) {
                mBottomDialog.show();
            } else {
                mGraceTimer = new Handler();
                mGraceTimer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mBottomDialog != null && !mFinished) {
                            mBottomDialog.show();
                        }
                    }
                }, mGraceTimeMs);
            }
        }
        return this;
    }

    public static BottomDialog create(Context context) {
        return new BottomDialog(context);
    }

    public void dismiss() {
        mFinished = true;
        if (mContext != null && mBottomDialog != null && mBottomDialog.isShowing()) {
            mBottomDialog.dismiss();
        }
        if (mGraceTimer != null) {
            mGraceTimer.removeCallbacksAndMessages(null);
            mGraceTimer = null;
        }
    }

    public boolean isShowing() {
        return mBottomDialog != null && mBottomDialog.isShowing();
    }


    public BottomDialog setStyle(Style sty) {
        View view = null;
        switch (sty) {
            case VERTICAL:
                view = new ContentLayout(mContext, mContents);

        }
        mBottomDialog.setView(view);
        return this;
    }

    private BottomDialog addContent(Content content) {
        if (content == null) return this;
        if (mContents == null) {
            mContents = new ArrayList<>();
        }
        mContents.add(content);
        return this;
    }

    public BottomDialog addContent(Content... contents) {

        for (Content c : contents) {
            addContent(c);
        }
        return this;
    }


    private BottomDialog addContents(List<Content> list) {
        mContents = list;
        return this;
    }

    public static class Content {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    private class SheetDialog extends Dialog {

        private SheetBackground mBackground;
        private FrameLayout mCustomViewContainer;
        private int mWidth, mHeight;
        private int mScale;
        private View mView;

        public SheetDialog(@NonNull Context context) {
            super(context);
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.bottom_sheet_dialog);
            Window window = getWindow();
            assert window != null;
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = mDimAmount;
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
            initView();
            mScale = getContext().getResources().getDisplayMetrics().densityDpi;
        }


        private void initView() {
            mBackground = findViewById(R.id.background);
            mBackground.setBaseColor(mWindowColor);
            mBackground.setCornerRadius(mCornerRadius);
            if (mWidth != 0) {
                updateBackgroundSize();
            }

            mCustomViewContainer = findViewById(R.id.container);
            addViewToFrame(mView);

        }

        private void updateBackgroundSize() {
            ViewGroup.LayoutParams layoutParams = mBackground.getLayoutParams();
            layoutParams.width = mScale * mWidth;
            layoutParams.height = mScale * mHeight;
            mBackground.setLayoutParams(layoutParams);
        }

        private void addViewToFrame(View view) {
            if (view == null) return;
            int wrapParam = ViewGroup.LayoutParams.WRAP_CONTENT;
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(wrapParam, wrapParam);
            mCustomViewContainer.addView(view);
        }

        public void setView(View view) {
            mView = view;
            if (isShowing()) {
                mCustomViewContainer.removeAllViews();
                addViewToFrame(view);
            }
        }

        public void setSize(int width, int height) {
            mWidth = width;
            mHeight = height;
            if (mBackground != null) {
                updateBackgroundSize();
            }
        }
    }
}
