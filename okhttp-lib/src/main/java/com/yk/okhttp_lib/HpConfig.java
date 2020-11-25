package com.yk.okhttp_lib;

import android.app.Application;

import com.yk.okhttp_lib.okhttp.ItgOk;
import com.yk.okhttp_lib.okhttp.interceptors.LoggerInterceptor;

public class HpConfig {
    public static void OkConfig(boolean openLogger, LoggerInterceptor.HL loggerLevel, String globalUrl,Application application) {
        ItgOk.initItgOk(openLogger, loggerLevel,globalUrl,application);
    }
}
