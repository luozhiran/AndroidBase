package com.yk.itg_util;

import android.os.Looper;

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
}
