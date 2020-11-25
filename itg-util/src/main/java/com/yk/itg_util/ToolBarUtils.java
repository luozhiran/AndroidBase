package com.yk.itg_util;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class ToolBarUtils {

    public static void hideBar(Context context) {
        if (context instanceof AppCompatActivity) {
            if (((AppCompatActivity) context).getSupportActionBar() != null) {
                ((AppCompatActivity) context).getSupportActionBar().hide();
            }
        } else if (context instanceof Activity) {
            if (((Activity) context).getActionBar() != null) {
                ((Activity) context).getActionBar().hide();
            }
        }
    }
}
