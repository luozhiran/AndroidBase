package com.yk.okhttp_lib.okhttp.operateimpl;


import com.yk.okhttp_lib.okhttp.ItemFile;
import com.yk.okhttp_lib.okhttp.VerificationUtils;
import com.yk.okhttp_lib.okhttp.operate.ItgBuilder;
import com.yk.okhttp_lib.okhttp.operate.MultiBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;

public class MultiBuilderImpl implements MultiBuilder {
    private List<ItemFile> okFiles;
    private ItgBuilder itgBuilder;
    private HashMap<String, String> okParams;
    private HashMap<String, Object> jsonMap;
    private String DEFAULT_MEDIA_TYPE = "application/octet-stream";

    public MultiBuilderImpl(ItgBuilder itgBuilder) {
        this.itgBuilder = itgBuilder;
    }

    @Override
    public ItgBuilder addMultiFile(String name, String file) {
        int count = file.indexOf("/");
        String fileName = "";
        if (count == -1 || count == file.length()) {
            fileName = file.substring(count);
        }
        addMultiFile(name, MediaType.parse(DEFAULT_MEDIA_TYPE), fileName, file);
        return itgBuilder;
    }

    @Override
    public ItgBuilder addMultiFile(String name, File file) {
        addMultiFile(name, MediaType.parse(DEFAULT_MEDIA_TYPE), file.getName(), file);
        return itgBuilder;
    }

    @Override
    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, File file) {
        if (fileName == null){
            fileName="";
        }
        getFile().add(new ItemFile(name, fileName, mediaType, file));
        return itgBuilder;
    }

    @Override
    public ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, String file) {
        if (fileName == null){
            fileName="";
        }
        getFile().add(new ItemFile(name, fileName, mediaType, file));
        return itgBuilder;
    }

    @Override
    public ItgBuilder addMultiParams(String key, String param) {
        getOkParams().put(key, param);
        return itgBuilder;
    }

    @Override
    public ItgBuilder addMultiJson(String key, Object json) {
        getJsonMap().put(key, json);
        return itgBuilder;
    }


    public Request build(Request.Builder builder) {
        MultipartBody.Builder muBody = new MultipartBody.Builder();
        muBody.setType(MultipartBody.FORM);
        if (okFiles != null) {
            VerificationUtils.multipartBodyFile(muBody, getFile());
        }
        if (okParams != null) {
            VerificationUtils.multipartBodyParams(muBody, getOkParams());
        }

        if (jsonMap != null) {
            VerificationUtils.multipartBodyJson(muBody, jsonMap);
        }
        return builder.post(muBody.build()).build();
    }

    private List<ItemFile> getFile() {
        if (okFiles == null) {
            okFiles = new ArrayList<>();
        }
        return okFiles;
    }

    private HashMap<String, String> getOkParams() {
        if (okParams == null) {
            okParams = new HashMap<>();
        }
        return okParams;
    }


    private HashMap<String, Object> getJsonMap() {
        if (jsonMap == null) {
            jsonMap = new HashMap<>();
        }
        return jsonMap;
    }

}
