package com.yk.itg_util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileIoUtils {

    /**
     * 读取assets目录下的文件
     * @param path
     * @param context
     * @return
     */
    public static String getAssetsFile(String path, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 获取assets目录下的图片
     * @param context 上下文
     * @param path  文件名
     * @return  Bitmap图片
     */
    public static Bitmap getAssetsBitmap(String path, Context context){
        Bitmap bitmap = null;
        AssetManager assetManager = context.getAssets();
        try {
            InputStream is = assetManager.open(path);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 获取assets目录下的单个文件
     * 这种方式只能用于webview加载
     * 读取文件夹，直接取路径是不行的
     * @param context 上下文
     * @param fileName  文件夹名
     * @return File
     */
    public static File getFileFromAssetsFile(Context context, String fileName){
        String path = "file:///android_asset/" + fileName;
        File file = new File(path);
        return file;
    }

}
