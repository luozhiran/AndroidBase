package com.yk.itg_util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.util.Locale;

public class AndroidUtils {

    public static boolean sdEnable() {
        String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(status)) {
            return true;
        } else {
            return false;
        }
    }

    public static StringBuilder getAndroidHardInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("手机系统语言:").append(Locale.getDefault().getLanguage()).append("\n");
        sb.append("系统版本号:").append(Build.VERSION.RELEASE).append("\n");
        sb.append("手机型号:").append(android.os.Build.MODEL).append("\n");
        sb.append("手机厂商:").append(android.os.Build.BRAND).append("\n");
        sb.append("HARDWARE:").append(Build.HARDWARE).append("\n");
        sb.append("HOST:").append(Build.HOST).append("\n");
        sb.append("ID:").append(Build.ID).append("\n");
        sb.append("TIME:").append(Build.TIME).append("\n");
        sb.append("TYPE:").append(Build.TYPE).append("\n");
        sb.append("USER:").append(Build.USER).append("\n");
        sb.append("MANUFACTURER:").append(Build.MANUFACTURER).append("\n");
        sb.append(supportABIS());
        return sb;
    }


    private static StringBuilder supportABIS() {
        StringBuilder sb = new StringBuilder();
        String str = "";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (String abi : Build.SUPPORTED_32_BIT_ABIS) {
                str += abi + ",";
            }
            sb.append("32_bit_abis").append(":").append(str).append("\n");
            str = "";
            for (String abi : Build.SUPPORTED_64_BIT_ABIS) {
                str += abi + ",";
            }
            sb.append("34_bit_abis").append(":").append(str).append("\n");
            str = "";
            for (String abi : Build.SUPPORTED_ABIS) {
                str += abi + ",";
            }
            sb.append("bit_abis").append(":").append(str).append("\n");
        }
        return sb;
    }

}
