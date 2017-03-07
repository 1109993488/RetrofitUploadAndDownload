package com.blingbling.retrofit.uploadanddownload.api;

import com.blingbling.retrofit.uploadanddownload.BuildConfig;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by BlingBling on 17/2/7.
 */
public abstract class BaseApi {

    //构造方法私有
    protected BaseApi() {
    }

    protected final <T> T api(String baseUrl, Class<T> cls) {
        final Retrofit.Builder builder = onCreateRetrofit();
        builder.baseUrl(baseUrl);
        builder.client(onCreateOkHttpClient().build());
        final T service = builder.build().create(cls);
        return service;
    }

    protected Retrofit.Builder onCreateRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(onCreateGson().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    protected OkHttpClient.Builder onCreateOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            //打印网络请求log日志
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder;
    }

    protected GsonBuilder onCreateGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls();
    }

}
