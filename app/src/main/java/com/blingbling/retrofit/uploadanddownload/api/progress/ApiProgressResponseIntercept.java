package com.blingbling.retrofit.uploadanddownload.api.progress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by BlingBling on 2017/3/7.
 */

public class ApiProgressResponseIntercept implements Interceptor {

    private ApiProgressListener mListener;

    public ApiProgressResponseIntercept(ApiProgressListener listener) {
        mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //拦截
        Response originalResponse = chain.proceed(chain.request());

        //包装响应体并返回
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), mListener))
                .build();
    }

    static class ProgressResponseBody extends ResponseBody {
        //实际的待包装响应体
        private final ResponseBody responseBody;
        //进度回调接口
        private final ApiProgressListener apiProgressListener;
        //包装完成的BufferedSource
        private BufferedSource bufferedSource;

        /**
         * 构造函数，赋值
         *
         * @param responseBody        待包装的响应体
         * @param apiProgressListener 回调接口
         */
        public ProgressResponseBody(ResponseBody responseBody, ApiProgressListener apiProgressListener) {
            this.responseBody = responseBody;
            this.apiProgressListener = apiProgressListener;
        }


        /**
         * 重写调用实际的响应体的contentType
         *
         * @return MediaType
         */
        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        /**
         * 重写调用实际的响应体的contentLength
         *
         * @return contentLength
         * @throws IOException 异常
         */
        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        /**
         * 重写进行包装source
         *
         * @return BufferedSource
         */
        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                //包装
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        /**
         * 读取，回调进度接口
         *
         * @param source Source
         * @return Source
         */
        private Source source(Source source) {
            return new ForwardingSource(source) {
                //当前读取字节数
                long totalBytesRead = 0L;
                //总字节长度，避免多次调用contentLength()方法
                long contentLength = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long byteRead = super.read(sink, byteCount);
                    if (contentLength == 0) {
                        //获得contentLength的值，后续不再调用
                        contentLength = contentLength();
                    }
                    //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                    totalBytesRead += byteRead != -1 ? byteRead : 0;
                    //回调，如果contentLength()不知道长度，会返回-1
                    if (apiProgressListener != null) {
                        apiProgressListener.onProgress(totalBytesRead, contentLength, byteRead == -1);
                    }
                    return byteRead;
                }
            };
        }
    }
}
