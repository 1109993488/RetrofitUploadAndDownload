package com.blingbling.retrofit.uploadanddownload.api;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 公共的请求
 * Created by BlingBling on 2017/3/10.
 */

public interface CHttpService {
    @GET
    Observable<String> GET(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<String> POST(@Url String url, @FieldMap Map<String, Object> params);
}
