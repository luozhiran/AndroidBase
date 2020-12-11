package com.plugin.widget.bottomSheet;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ContentLayout extends LinearLayout {
    private List<BottomDialog.Content> list;

    public ContentLayout(Context context, List<BottomDialog.Content> contents) {
        super(context);
        list = contents;
        init();
    }


    private void init() {
        setOrientation(VERTICAL);
        BottomDialog.Content content = null;
        for (int i = 0; i < list.size(); i++) {
            content = list.get(i);
            TextView tv = new TextView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            tv.setText(content.getText());
            addView(tv);
        }
    }
}
