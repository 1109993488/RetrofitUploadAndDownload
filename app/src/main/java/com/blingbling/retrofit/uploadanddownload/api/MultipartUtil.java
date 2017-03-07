package com.blingbling.retrofit.uploadanddownload.api;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by BlingBling on 2017/3/7.
 */

public class MultipartUtil {

    private static final String MEDIA_TYPE_TEXT = "text/plain; charset=UTF-8";

    public static MediaType getFileMediaType(String fileName) {
        final FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return MediaType.parse(contentTypeFor);
    }

    public static MultipartBody.Part getMultipart(String key, File file) {
        final MediaType mediaType = MultipartUtil.getFileMediaType(file.getAbsolutePath());
        final RequestBody body = RequestBody.create(mediaType, file);
        return MultipartBody.Part.createFormData(key, file.getName(), body);
    }

    public static Map<String, RequestBody> getRequestBodyMap(Map<String, ?> options, String fileKey, File file) {
        ArrayList<File> files = new ArrayList<>();
        files.add(file);
        return getRequestBodyMap(options, fileKey, files);
    }

    public static Map<String, RequestBody> getRequestBodyMap(Map<String, ?> options, String fileKey, List<File> files) {
        final HashMap<String, List<File>> fileMap = new HashMap<>();
        fileMap.put(fileKey, files);
        return getRequestBodyMap(options, fileMap);
    }

    /**
     * 适合<P>@PartMap {@link Map} body</P>
     *
     * @param options
     * @param fileMaps
     * @return
     */
    public static Map<String, RequestBody> getRequestBodyMap(Map<String, ?> options, Map<String, List<File>> fileMaps) {
        final Map<String, RequestBody> body = new HashMap<>();
        if (options != null) {
            for (Map.Entry<String, ?> entry : options.entrySet()) {
                final Object obj = entry.getValue();
                if (obj != null) {
                    body.put(entry.getKey(), RequestBody.create(MediaType.parse(MEDIA_TYPE_TEXT), obj.toString()));
                }
            }
        }
        if (fileMaps != null) {
            for (Map.Entry<String, List<File>> entry : fileMaps.entrySet()) {
                final List<File> files = entry.getValue();
                if (files != null) {
                    for (int i = 0, size = files.size(); i < size; i++) {
                        final File file = files.get(i);
                        final MediaType mediaType = MultipartUtil.getFileMediaType(file.getAbsolutePath());
                        final RequestBody fileBody = RequestBody.create(mediaType, file);
                        body.put(entry.getKey() + "\"; filename=\"" + file.getName(), fileBody);
                    }
                }
            }
        }
        return body;
    }
}
