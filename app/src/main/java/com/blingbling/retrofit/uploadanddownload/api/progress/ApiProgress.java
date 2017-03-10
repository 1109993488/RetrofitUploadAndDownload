package com.blingbling.retrofit.uploadanddownload.api.progress;

/**
 * Created by BlingBling on 2017/3/10.
 */

public class ApiProgress {
    public long currentSize;
    public long totalSize;
    public boolean done;

    public ApiProgress(long currentSize, long totalSize, boolean done) {
        this.currentSize = currentSize;
        this.totalSize = totalSize;
        this.done = done;
    }
}
