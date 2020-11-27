package com.plugin.itg_util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.io.File;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

public class ApkUtils {

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Android apk教程
     * if(isInstallPermission(context)){
     *     install(path);
     * }else{
     *     开启可以安装位置来源权限,可以给用户适当的提示框，进行解释说明
     *     gotoSettingUiForInstall();
     * }
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 是否有安装权限
     *
     * @return
     */
    public static boolean isInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //兼容8.0安装位置来源的权限
            return context.getPackageManager().canRequestPackageInstalls();
        } else {
            return true;
        }
    }


    /**
     * 进入设置界面，选择同意安装位置来源权限
     * @param activity
     * @param requestCode
     */
    public static void gotoSettingUiForInstall(Activity activity,int requestCode){
        Uri packageUri = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * android 7.0以后，访问共享文件，必须开启临时权限 fileprovider
     *       <provider
     *             android:name="androidx.core.content.FileProvider"
     *             android:authorities="com.yk.surveyor.fileprovider"
     *             android:exported="false"
     *             android:grantUriPermissions="true">
     *             <meta-data
     *                 android:name="android.support.FILE_PROVIDER_PATHS"
     *                 android:resource="@xml/file_paths" />
     *         </provider>
     * @param path
     * @param authority
     */
    public static void install(String path,String authority,Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, authority, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    //获取版本号
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static long getApkVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            long versionCode = info.getLongVersionCode();
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取版本名称
    public static String getApkVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取apk包的信息：版本号，名称，图标等
     *
     * @param absPath
     * @return
     */
    public static String getApkInfo(String absPath,Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo packageInfo = manager.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = manager.getApplicationLabel(appInfo).toString();// 得到应用名 
            String packageName = appInfo.packageName; // 得到包名 
            String version = packageInfo.versionName; // 得到版本信息 
            /* icon1和icon2其实是一样的 */
            Drawable icon1 = manager.getApplicationIcon(appInfo);// 得到图标信息 
            Drawable icon2 = appInfo.loadIcon(manager);
            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);
            return pkgInfoStr;
        }
        return null;

    }
}
