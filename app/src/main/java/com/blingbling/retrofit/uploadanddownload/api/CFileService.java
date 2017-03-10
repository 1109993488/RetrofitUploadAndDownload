package com.blingbling.retrofit.uploadanddownload.api;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by BlingBling on 2017/3/10.
 */

public interface CFileService {
    //上传图片
    @Multipart
    @POST
    Observable<String> uploadPhoto(@Url String url, @Part MultipartBody.Part file);

    //上传图片
    @Multipart
    @POST
    Observable<String> uploadPhoto(@Url String url, @QueryMap Map<String, String> options, @Part MultipartBody.Part file);

    //上传多图或单图
    @Multipart
    @POST("upload")
    Observable<String> uploadPhoto(@Url String url, @PartMap Map<String, RequestBody> body);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);
}
