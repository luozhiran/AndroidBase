package com.plugin.itg_util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.itg.lib_log.L;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileIoUtils {

    /**
     * 读取assets目录下的文件
     *
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
     *
     * @param context 上下文
     * @param path    文件名
     * @return Bitmap图片
     */
    public static Bitmap getAssetsBitmap(String path, Context context) {
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
     *
     * @param context  上下文
     * @param fileName 文件夹名
     * @return File
     */
    public static File getFileFromAssetsFile(Context context, String fileName) {
        String path = "file:///android_asset/" + fileName;
        File file = new File(path);
        return file;
    }


    public static void delete(String str) {
        File file = new File(str);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFile = file.listFiles();
                if (subFile != null) {
                    for (File f : subFile) {
                        if (f.isFile()) {
                            f.delete();
                        } else if (f.isDirectory() && f.exists()) {
                            delete(f.getAbsolutePath());
                            f.delete();
                        }
                    }
                }
            } else {
                file.delete();
            }

        }
    }


    public static void write(InputStream inputStream, String path) {
        File file = new File(path);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);

            int result = 0;
            byte[] cache = new byte[1024];
            while ((result = inputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, result);
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
