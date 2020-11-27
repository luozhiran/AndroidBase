package com.plugin.androidbase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import com.itg.lib_log.L;
import com.plugin.androidbase.databinding.ActivityMainBinding;
import com.plugin.androidbase.datas.NewsCategoryData;
import com.plugin.androidbase.model.FragmentStatePageAdapterViewModel;
import com.plugin.itg_util.ToolBarUtils;
import com.plugin.okhttp_lib.HpConfig;
import com.plugin.okhttp_lib.okhttp.ItgOk;
import com.plugin.okhttp_lib.okhttp.Ok;
import com.plugin.okhttp_lib.okhttp.interceptors.LoggerInterceptor;

import java.io.File;
import java.io.IOException;


//https://tech.meituan.com/2016/11/11/android-mvvm.html
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FragmentStatePageAdapterViewModel newsViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HpConfig.OkConfig(true, LoggerInterceptor.HL.BODY,"http://192.168.40.163:8080",getApplication());
        requestPermission();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ToolBarUtils.hideBar(this);
        newsViewModel = new ViewModelProvider(this).get(FragmentStatePageAdapterViewModel.class);
        binding.setModel(newsViewModel);
        newsViewModel.getNewsCategory();
        binding.tab.setupWithViewPager(binding.viewpager);


    }


    private void initScan() {

        NewsCategoryData data = new NewsCategoryData();
        data.setName("罗志然");
        data.setA(1111);
        data.setB(3.9493);

        NewsCategoryData a = new NewsCategoryData();
        a.setName("罗志然");
        a.setA(1111);
        a.setB(3.9493);
        data.setData(a);

        File file = new File(Environment.getExternalStorageDirectory() + "/abc.txt");
        if (!file.exists()) throw new IllegalArgumentException("ddddddddd");
        ItgOk.instance()
                .url("http://192.168.40.163:8080/test/multipart1")
                .addHeader("abc", "abc")
                .addMultiFile("file", file)
                .autoCancel(getLifecycle())
                .method(Ok.POST)
                .ok()
                .go(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        L.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        L.e(response.body().string());
                    }
                });
    }

    private void requestPermission() {
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            initScan();
        } else {
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //通过requestCode来识别是否同一个请求
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作
                initScan();
            } else {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage("d")
                            .setPositiveButton("OK", (dialog1, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            101))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }
}

