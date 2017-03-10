package com.blingbling.retrofit.uploadanddownload.api;

import android.os.Handler;
import android.os.Looper;

import com.blingbling.retrofit.uploadanddownload.BuildConfig;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressListener;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressRequestIntercept;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressResponseIntercept;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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
    protected OkHttpClient.Builder onCreateOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
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
    protected void onCreateLoggingInterceptor(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            //打印网络请求log日志
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            builder.addInterceptor(interceptor);
        }
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
