package com.blingbling.retrofit.uploadanddownload;

import com.blingbling.retrofit.uploadanddownload.api.MultipartUtil;
import com.blingbling.retrofit.uploadanddownload.api.intercept.ProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BlingBling on 2017/3/7.
 */

public class FileApiModel {

    public static Observable<String> uploadPhoto1(File file, ProgressListener listener) {
        return FileApi.service(true, listener).uploadPhoto(MultipartUtil.getMultipart("file", file))
                .compose(FileApiModel.<String>io());
    }

    public static Observable<String> uploadPhoto2(Map<String, String> options, File file, ProgressListener listener) {
        return FileApi.service(true, listener).uploadPhoto(options, MultipartUtil.getMultipart("file", file))
                .compose(FileApiModel.<String>io());
    }

    public static Observable<String> uploadPhoto3(Map<String, String> options, List<File> files, ProgressListener listener) {
        return FileApi.service(true, listener).uploadPhoto(MultipartUtil.getRequestBodyMap(options, "file", files))
                .compose(FileApiModel.<String>io());
    }

    public static Observable<File> download(String url, final File saveFile, ProgressListener listener) {
        return FileApi.service(false, listener).download(url)
                .flatMap(saveFileFunc1(saveFile))
                .compose(FileApiModel.<File>io());
    }

    public static Func1<ResponseBody, Observable<File>> saveFileFunc1(final File saveFile) {
        return new Func1<ResponseBody, Observable<File>>() {
            @Override
            public Observable<File> call(ResponseBody responseBody) {

                InputStream inputStream = responseBody.byteStream();
                FileOutputStream fos = null;
                try {
                    final File parentFile = saveFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    fos = new FileOutputStream(saveFile);
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("保存文件异常");
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
                return Observable.just(saveFile);
            }
        };
    }

    private static <T> Observable.Transformer<T, T> io() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
