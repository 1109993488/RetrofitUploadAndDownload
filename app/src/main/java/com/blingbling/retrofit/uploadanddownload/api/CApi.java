package com.blingbling.retrofit.uploadanddownload.api;

import com.blingbling.retrofit.uploadanddownload.BuildConfig;
import com.google.gson.Gson;
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
public abstract class CApi {

    //构造方法私有
    protected CApi() {
    }

    /**
     * 普通请求可以共用一个service，如果上传或下载需要重新创建新的service
     *
     * @param baseUrl
     * @param cls
     * @param <T>
     * @return
     */
    protected final <T> T createService(String baseUrl, Class<T> cls) {
        final Retrofit.Builder retrofitBuilder = onCreateRetrofit();
        final OkHttpClient.Builder okHttpClientBuilder = onCreateOkHttpClient();

        onCreateLoggingInterceptor(okHttpClientBuilder);

        retrofitBuilder.baseUrl(baseUrl)
                .client(okHttpClientBuilder.build());

        return retrofitBuilder.build().create(cls);
    }

    protected Retrofit.Builder onCreateRetrofit() {
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(onCreateGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        return builder;
    }

    protected OkHttpClient.Builder onCreateOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        return builder;
    }

    protected Gson onCreateGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
    }

    protected void onCreateLoggingInterceptor(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            //打印网络请求log日志
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
    }
}
