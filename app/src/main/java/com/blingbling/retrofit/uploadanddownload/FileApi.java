package com.blingbling.retrofit.uploadanddownload;

import com.blingbling.retrofit.uploadanddownload.api.BaseFileApi;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressListener;
import com.blingbling.retrofit.uploadanddownload.service.FileService;

/**
 * Created by BlingBling on 2017/3/8.
 */

public class FileApi extends BaseFileApi {

    private static final String BASE_URL = "http://10.2.201.42:8080/";

    public static FileService service(boolean upload, ProgressListener listener) {
        return new FileApi(upload, listener).api(BASE_URL, FileService.class);
    }

    protected FileApi(boolean upload, ProgressListener listener) {
        super(upload, listener);
    }
}
