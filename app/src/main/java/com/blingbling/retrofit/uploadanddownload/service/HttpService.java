package com.blingbling.retrofit.uploadanddownload.service;


import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface HttpService {

    @GET
    Observable<String> GET(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<String> POST(@Url String url, @FieldMap Map<String, Object> params);

}