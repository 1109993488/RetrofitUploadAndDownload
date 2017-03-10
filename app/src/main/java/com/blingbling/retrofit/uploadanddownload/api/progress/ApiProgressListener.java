package com.blingbling.retrofit.uploadanddownload.api.progress;

/**
 * 请求体进度回调接口，用于文件上传下载进度回调
 * Created by BlingBling on 2017/3/7.
 */
public interface ApiProgressListener {
    void onProgress(long currentSize, long totalSize, boolean done);
}