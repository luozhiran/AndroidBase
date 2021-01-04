package com.plugin.itg_util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

public class Utils {

    public static boolean isMainThread(){
        if (Looper.getMainLooper() == Looper.myLooper()){
            return true;
        }else {
            return false;
        }
    }

    public static void ensureChangeOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("You must only modify the ObservableList on the main thread.");
        }
    }



    public static String[] checkPermission(Context context, String[] permissions) {
        List<String> list = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            list = new ArrayList<>();
            for (String s : permissions) {
                if (ActivityCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                    list.add(s);
                }
            }
        }
        if (list != null) {
            return list.toArray(new String[]{});
        }
        return null;
    }

    public static void requestPermission(String[] needPermission, Activity activity, int code) {
        if (needPermission != null && needPermission.length > 0) {
            ActivityCompat.requestPermissions(activity, needPermission, 11);
        }
    }
}
