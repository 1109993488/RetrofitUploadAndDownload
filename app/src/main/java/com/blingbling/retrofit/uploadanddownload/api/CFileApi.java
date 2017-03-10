package com.blingbling.retrofit.uploadanddownload.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;

import com.blingbling.retrofit.uploadanddownload.BuildConfig;
import com.blingbling.retrofit.uploadanddownload.api.progress.ApiProgress;
import com.blingbling.retrofit.uploadanddownload.api.progress.ApiProgressRequestIntercept;
import com.blingbling.retrofit.uploadanddownload.api.progress.ApiProgressResponseIntercept;
import com.blingbling.retrofit.uploadanddownload.api.progress.ApiProgressListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by BlingBling on 2017/3/7.
 */

public abstract class CFileApi extends CApi implements ApiProgressListener {

    public static final int TYPE_UPLOAD = 1;
    public static final int TYPE_DOWNLOAD = 2;

    @IntDef({TYPE_UPLOAD, TYPE_DOWNLOAD})
    public @interface ApiType {
    }

    private int mType;

    private static final int WHAT_UPDATE_PROGRESS = 1;
    private ApiProgressListener mApiProgressListener;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_UPDATE_PROGRESS) {
                final ApiProgress progress = (ApiProgress) msg.obj;
                mApiProgressListener.onProgress(progress.currentSize, progress.totalSize, progress.done);
            }
        }
    };

    /**
     * @param type     {@link #TYPE_UPLOAD}为上传，{@link #TYPE_DOWNLOAD}为下载
     * @param listener
     */
    protected CFileApi(@ApiType int type, ApiProgressListener listener) {
        mType = type;
        mApiProgressListener = listener;
    }

    @Override
    protected OkHttpClient.Builder onCreateOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        if (mType == TYPE_UPLOAD) {
            builder.addInterceptor(new ApiProgressRequestIntercept(this));
        } else if (mType == TYPE_DOWNLOAD) {
            builder.addInterceptor(new ApiProgressResponseIntercept(this));
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
        if (mApiProgressListener != null) {
            mHandler.obtainMessage(WHAT_UPDATE_PROGRESS, new ApiProgress(currentSize, totalSize, done)).sendToTarget();
        }
    }
}
