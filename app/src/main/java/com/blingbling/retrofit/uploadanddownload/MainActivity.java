package com.blingbling.retrofit.uploadanddownload;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    File file = new File(Environment.getExternalStorageDirectory(), "zzz/1.jpg");
    File file2 = new File(Environment.getExternalStorageDirectory(), "zzz/2.jpg");

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public Subscriber<String> getSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError", e);

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext---->" + s);

            }
        };
    }

    public ProgressListener getProgressListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(long currentSize, long totalSize, boolean done) {
                Log.e(TAG, "onProgress--" + currentSize + "  " + totalSize + "  " + done);
                mProgressBar.setMax(100);
                mProgressBar.setProgress((int) ((100D * currentSize) / totalSize));
            }
        };
    }

    //简单上传
    public void click1(View view) {
        FileApiModel.uploadPhoto1(file, getProgressListener()).subscribe(getSubscriber());
    }

    //简单上传有请求数据
    public void click2(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "33");
        FileApiModel.uploadPhoto2(map, file, getProgressListener()).subscribe(getSubscriber());
    }

    //上传多图
    public void click3(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "33");
        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);
        FileApiModel.uploadPhoto3(map, files, getProgressListener()).subscribe(getSubscriber());
    }

    //下载
    public void click4(View view) {
        String url = "http://sw.bos.baidu.com/sw-search-sp/software/cac747f0354a4/BaiduNetdisk_mac_2.0.1.dmg";
        File saveFile = new File(Environment.getExternalStorageDirectory(), "zzz/save.dmg");
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }
        FileApiModel.download(url, saveFile, getProgressListener()).subscribe(new Subscriber<File>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError", e);
            }

            @Override
            public void onNext(File file) {
                Log.e(TAG, "onNext--->" + file);
            }
        });
        List<String> strings = gett();
    }

    public <T> List<T> gett() {
        return null;
    }
}
