package com.plugin.okhttp_lib.okhttp;


import com.plugin.okhttp_lib.okhttp.body.JsonBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class VerificationUtils {


    public static void requestBody(Request.Builder builder, List<ItemFile> fileList, HashMap<String, String> params, Object jsonObj) {
        if (fileList.size() == 0) {
            if (params.size() > 0) {
                formBodyParams(builder, params);
            }
            if (jsonObj != null) {
                jsonBodyJson(builder, jsonObj);
            }
        } else {
            MultipartBody.Builder muBody = new MultipartBody.Builder();
            muBody.setType(MultipartBody.FORM);
            multipartBodyFile(muBody, fileList);

            multipartBodyParams(muBody, params);
            builder.post(muBody.build());
        }

    }

    public static void multipartBodyParams(MultipartBody.Builder builder, HashMap<String, String> params) {
        for (Map.Entry<String, String> ent : params.entrySet()) {
            builder.addFormDataPart(ent.getKey(), ent.getValue());
        }
    }


    public static void multipartBodyFile(MultipartBody.Builder builder, List<ItemFile> fileList) {
        RequestBody requestBody = null;
        for (ItemFile file : fileList) {
            if (file.getContent() instanceof File) {
                File contentFile = (File) file.getContent();
                requestBody = RequestBody.create(file.getMediaType(), contentFile);
            } else if (file.getContent() instanceof String) {
                File contentFile = new File((String) file.getContent());
                requestBody = RequestBody.create(file.getMediaType(), contentFile);
            }
            builder.addFormDataPart(file.getName(), file.getFileName(), requestBody);
        }

    }


    public static void multipartBodyJson(MultipartBody.Builder builder, HashMap<String, Object> jsonObj) {
        if (jsonObj != null) {
            for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                builder.addFormDataPart(entry.getKey(), null, new JsonBody(entry.getValue()));
            }
        }
    }

    /**
     * FormBody 服务器接收格式 key=value&key=value
     *
     * @param builder
     * @param params
     */
    public static void formBodyParams(Request.Builder builder, HashMap<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> ent : params.entrySet()) {
            formBuilder.add(ent.getKey(), ent.getValue());
        }
        builder.post(formBuilder.build());
    }

    public static void jsonBodyJson(Request.Builder builder, Object jsonObj) {
        builder.post(new JsonBody(jsonObj));
    }


    public static void url(Request.Builder builder, Map<String, String> map) {
        builder.get();
        HttpUrl.Builder newBuilder = null;
        for (Map.Entry<String, String> ent : map.entrySet()) {
            if (newBuilder == null) {
                newBuilder = builder.build().url().newBuilder();
            }
            String obj = ent.getValue();
            newBuilder.addQueryParameter(ent.getKey(), obj);
        }
        if (newBuilder != null) {
            builder.url(newBuilder.build());
        }
    }


}
