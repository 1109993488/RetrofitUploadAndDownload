package com.blingbling.retrofit.uploadanddownload.api.intercept;

/**
 * 请求体进度回调接口，用于文件上传下载进度回调
 * Created by BlingBling on 2017/3/7.
 */
public interface ProgressListener {
    void onProgress(long currentSize, long totalSize, boolean done);
}