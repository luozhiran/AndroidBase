package com.plugin.itg_util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.itg.lib_log.L;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class InvokeSystemCameraUtils {
    private int REQUESTCODE_TAKE = 1000;
    private int REQUESTCODE_PICK = 10001;
    private int REQUESTCODE_CUTTING = 1002;
    private Context mContext;
    private String mTakePhotoSavePath;
    private String mTakePhotoPhotoName;
    private int mCropHeight = 600, mCropWidth = 600;
    private String mAuthority;


    public static InvokeSystemCameraUtils create(Context application, String authority) {

        return new InvokeSystemCameraUtils(application, authority);
    }

    private InvokeSystemCameraUtils(Context context, String authority) {
        mContext = context;
        mAuthority = authority;

    }

    public interface OnGalleryResultCallback {
        void onResultPhone(Uri uri, String orgImgPath, String compressImgPath);

        // 取得裁剪后的图片
        void onResultFromCrop(String path);
    }


    /**
     * 剪切图片的路径
     */
    private String mCropPath;
    /**
     * 剪切图片的名称
     */
    private String mCropName;


    /**
     * 拍照保存路径
     *
     * @param path
     * @return
     */
    public InvokeSystemCameraUtils takePhotoSavePath(String path) {
        mTakePhotoSavePath = path;
        return this;
    }


    /**
     * 拍照后图片的名称
     *
     * @param name
     * @return
     */
    public InvokeSystemCameraUtils takePhotoPhotoName(String name) {
        mTakePhotoPhotoName = name;
        return this;
    }

    /**
     * 剪切图片路径
     *
     * @param cropPath
     * @return
     */
    public InvokeSystemCameraUtils cropImgPath(String cropPath) {
        mCropPath = cropPath;
        return this;
    }

    /**
     * 剪切图片名称
     *
     * @param cropName
     * @return
     */
    public InvokeSystemCameraUtils corpImgName(String cropName) {
        mCropName = cropName;
        return this;
    }

    public InvokeSystemCameraUtils cropSize(int cropWidth, int cropHeight) {
        mCropHeight = cropHeight;
        mCropWidth = cropWidth;
        return this;
    }


    public String getSaveImgPath() {
        return mTakePhotoSavePath + mTakePhotoPhotoName;
    }

    public String getCrpPath() {
        return mCropPath + mCropName;
    }


    public void gotoCameraForResult(Activity activity, int request) {
        // 创建打开系统相机的意图
        REQUESTCODE_TAKE = request;
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置参数。将拍照所得的照片存到磁盘中；照片文件的位置，此处是在外置SD卡
        Uri uri;
        File file = new File(mTakePhotoSavePath, mTakePhotoPhotoName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(activity, mAuthority, file);
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 调用系统相机
        activity.startActivityForResult(intentCamera, REQUESTCODE_TAKE);
    }


    /**
     * 进入系统相册
     *
     * @param activity
     */
    public void gotoGallery(Activity activity, int request) {
        REQUESTCODE_PICK = request;
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, REQUESTCODE_PICK);
    }


    /**
     * 系统相机拍照，图片很大，会消耗大量内存，容易引发oom
     * 自定义压缩
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param callback
     */
    public void customCompress(int requestCode, int resultCode, Intent data, OnGalleryResultCallback callback) {

        if (requestCode == REQUESTCODE_PICK) {
            try {
                Uri uri = data.getData();
                String path = getRealPathFromUri(uri);
                callback.onResultPhone(uri, path, path);
            } catch (NullPointerException e) {
                e.printStackTrace();// 用户点击取消操作时的异常
            }
        } else if (requestCode == REQUESTCODE_TAKE) {
            Uri uri = null;
            File file = new File(mTakePhotoSavePath, mTakePhotoPhotoName);
            if (resultCode == Activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(mContext, mAuthority, file);
                } else {
                    uri = Uri.fromFile(file);
                }
            }
            String orgImgPath = file.getAbsolutePath();
            String compressImgPath = null;
            Bitmap bitmap = getScaleBitmap(uri, 800, 400, null, true);//对图片进行缩放
            bitmap = rotateBitmap(bitmap, orgImgPath);//系统相机拍照，可能图片的方向和在imageview上显示的方向不同，这里进行旋转
            compressImgPath = new File(mTakePhotoSavePath, "compress_" + mTakePhotoPhotoName).getAbsolutePath();
            compressImage(bitmap);
            saveBitmap(bitmap, compressImgPath);
            callback.onResultPhone(uri, orgImgPath, compressImgPath);
        } else if (requestCode == REQUESTCODE_CUTTING) {
            if (data != null) {
                callback.onResultFromCrop(mCropPath + mCropName);
            }
            File file = new File(getSaveImgPath());
            if (file.exists()) {
                file.delete();
            }
        }

    }


    /**
     * 第三方工具压缩
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param callback
     */
    public void luBanCompress(int requestCode, int resultCode, Intent data, OnGalleryResultCallback callback) {

        if (requestCode == REQUESTCODE_PICK) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = getRealPathFromUri(uri);
                    startLuBanCompress(uri, new File(path), callback);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();// 用户点击取消操作时的异常
            }
        } else if (requestCode == REQUESTCODE_TAKE) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(mTakePhotoSavePath, mTakePhotoPhotoName);
                startLuBanCompress(null, file, callback);
            }
        } else if (requestCode == REQUESTCODE_CUTTING) {
            if (data != null) {
                callback.onResultFromCrop(mCropPath + mCropName);
            }
            File file = new File(getSaveImgPath());
            if (file.exists()) {
                file.delete();
            }
        }
    }


    private void startLuBanCompress(final Uri uri, final File origFile, final OnGalleryResultCallback callback) {
        Luban.with(mContext).load(origFile)
                .ignoreBy(500)
                .setTargetDir(mTakePhotoSavePath)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
//
                    }

                    @Override
                    public void onSuccess(File file) {
                        Uri newUri = uri;
                        if (newUri == null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(mContext, mAuthority, origFile);
                            } else {
                                newUri = Uri.fromFile(origFile);
                            }
                        }
                        callback.onResultPhone(newUri, origFile.getAbsolutePath(), file.getAbsolutePath());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri 要裁剪的图片的URI
     */

    public void startPhotoZoom(Uri uri, Activity activity, int requestCode) {
        if (TextUtils.isEmpty(mCropPath)) {
            mCropPath = mTakePhotoSavePath;
            mCropName = "crop" + mTakePhotoPhotoName;
        }
        REQUESTCODE_CUTTING = requestCode;
        File file = new File(mCropPath, mCropName);
        Intent intent = new Intent("com.android.camera.action.CROP");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，这里可以将宽高作为参数传递进来
        intent.putExtra("outputX", mCropWidth);
        intent.putExtra("outputY", mCropHeight);

        // 其实加上下面这两句就可以实现基本功能，
        //但是这样做我们会直接得到图片的数据，以bitmap的形式返回，在Intent中。而Intent传递数据大小有限制，1kb=1024字节，这样就对最后的图片的像素有限制。
        //intent.putExtra("return-data", true);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);

        // 解决不能传图片，Intent传递数据大小有限制，1kb=1024字节
        // 方法：裁剪后的数据不以bitmap的形式返回，而是放到磁盘中，更方便上传和本地缓存
        // 设置裁剪后的数据不以bitmap的形式返回，剪切后图片的位置，图片是否压缩等
        intent.putExtra("return-data", false);//是否在Intent中返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        Uri outUri = null;
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            outUri = FileProvider.getUriForFile(this, "com.yk.surveyor.fileprovider", mCropImageFile);
//        } else {
//            outUri = Uri.fromFile(mCropImageFile);
//        }
        outUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        // 调用系统的图片剪切
        activity.startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 在onDestroy中释放
     */
    public void release() {
        mTakePhotoSavePath = null;
        mTakePhotoPhotoName = null;
        mCropPath = null;
        mCropName = null;
    }


    /**
     * 根据Uri获取图片的绝对路径
     * 从手机相册获取图片
     *
     * @param uri 图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public String getRealPathFromUri(Uri uri) {
        String path = getRealPathFromUri(mContext, uri);
        L.e("path " + path);
        return path;
    }


    /**
     * 获取图片，原图太大容易造成oom,对读取的图片做缩放处理
     *
     * @param uri
     * @param scaleW 缩放比例
     * @param scaleH 缩放比例
     * @param config 图片编码 Bitmap.Config.RGB_565,Bitmap.Config.ARGB_8888
     * @param scale  所否缩放，false 返回原图
     * @return 返回缩放过的图片
     */
    public Bitmap getScaleBitmap(Uri uri, float scaleW, float scaleH, @Nullable Bitmap.Config config, boolean scale) {
        InputStream input = null;
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            if (scale) {
                //设置缩放比例
                bitmapOptions.inSampleSize = getScaleRate(uri, scaleW, scaleH);
            }
            bitmapOptions.inDither = true;
            if (config != null) {
                bitmapOptions.inPreferredConfig = config;
            }

            input = mContext.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            assert input != null;
            input.close();
            return bitmap;//再进行质量压缩
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图片缩放比例
     * 加载图片时，不加载到内存中
     *
     * @return 1表示不缩放
     */
    private int getScaleRate(Uri uri, float scaleW, float scaleH) {
        InputStream input = null;
        try {
            input = mContext.getContentResolver().openInputStream(uri);
            //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;//不加载到内存
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            int be = 1;//be=1表示不缩放
            if ((originalWidth == -1) || (originalHeight == -1))//无法获取图片宽高
                return be;
//            图片分辨率以480x800为标准
//            float hh = 800f;//这里设置高度为800f
//            float ww = 480f;//这里设置宽度为480f
            if (originalWidth > originalHeight && originalWidth > scaleW) {//如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / scaleW);
            } else if (originalWidth < originalHeight && originalHeight > scaleH) {//如果高度高的话根据宽度固定大小缩放
                be = (int) (originalHeight / scaleH);
            }
            if (be <= 0)//be只能是整数
                be = 1;
            return be;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 1;
    }


    private Bitmap rotateBitmap(Bitmap bitmap, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(path);
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
                if (digree != 0) {
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    bitmap.recycle();
                    return rotateBitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options <= 0)
                break;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public void saveBitmap(Bitmap bitmap, String path) {
        if (bitmap != null) return;
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = path;
        } else {
            Log.e("tag", "saveBitmap failure : sdcard not mounted");
            return;
        }
        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            return;
        }
    }


    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) { // api >= 19
            return getImageAbsolutePath(context, uri);
        } else { // api < 19
            return getPathFromUri(context, uri);
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        String path = null;
        if (uri != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                path = getImageAbsolutePath(context, uri);
            } else {
                Cursor cursor = null;
                try {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    cursor = context.getContentResolver().query(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(column_index);
                } catch (Exception e) {
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }

        }
        return path;
    }


    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
