package com.blingbling.retrofit.uploadanddownload;

import com.blingbling.retrofit.uploadanddownload.api.CFileApi;
import com.blingbling.retrofit.uploadanddownload.api.progress.ApiProgressListener;
import com.blingbling.retrofit.uploadanddownload.service.FileService;

/**
 * Created by BlingBling on 2017/3/8.
 */

public class FileApi extends CFileApi {

    private static final String BASE_URL = "http://10.2.201.42:8080/";

    public static FileService service(int type, ApiProgressListener listener) {
        return new FileApi(type, listener).createService(BASE_URL, FileService.class);
    }

    /**
     * @param type     {@link #TYPE_UPLOAD}为上传，{@link #TYPE_DOWNLOAD}为下载
     * @param listener
     */
    protected FileApi(@ApiType int type, ApiProgressListener listener) {
        super(type, listener);
    }

}
