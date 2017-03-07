package com.blingbling.retrofit.uploadanddownload.api;

import android.os.Handler;
import android.os.Looper;

import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressListener;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressRequestIntercept;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressResponseIntercept;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by BlingBling on 2017/3/7.
 */

public abstract class BaseFileApi extends BaseApi implements ProgressListener {

    private boolean mUpload;
    private ProgressListener mProgressListener;
    private Handler mHandler;

    /**
     * @param upload   true为上传，false为下载
     * @param listener
     */
    protected BaseFileApi(boolean upload, ProgressListener listener) {
        mUpload = upload;
        mProgressListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected Retrofit.Builder onCreateRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    @Override
    protected OkHttpClient.Builder onCreateOkHttpClient() {
        OkHttpClient.Builder builder = super.onCreateOkHttpClient();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        if (mUpload) {
            builder.addInterceptor(new ProgressRequestIntercept(this));
        } else {
            builder.addInterceptor(new ProgressResponseIntercept(this));
        }
        return builder;
    }

    @Override
    public void onProgress(final long currentSize, final long totalSize, final boolean done) {
        if (mProgressListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressListener.onProgress(currentSize, totalSize, done);
                }
            });
        }
    }
}
