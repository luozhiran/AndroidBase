package com.yk.okhttp_lib.okhttp.operateimpl;

import com.yk.okhttp_lib.okhttp.Ok;
import com.yk.okhttp_lib.okhttp.VerificationUtils;
import com.yk.okhttp_lib.okhttp.operate.ItgBuilder;
import com.yk.okhttp_lib.okhttp.operate.SingleBuilder;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SingleBuilderImpl implements SingleBuilder {
    private Object jsonObj;
    private ItgBuilder itgBuilder;
    private HashMap<String, String> okParams;
    private File uploadFile;
    private String content;
    private boolean already;

    public SingleBuilderImpl(ItgBuilder itgBuilder) {
        this.itgBuilder = itgBuilder;
    }

    @Override
    public ItgBuilder addParams(String key, String params) {
        if (okParams == null) {
            check();
        }
        getOkParams().put(key, params);
        already = true;
        return itgBuilder;
    }

    @Override
    public ItgBuilder addFile(String file) {
        check();
        already = true;
        uploadFile = new File(file);
        return itgBuilder;
    }

    @Override
    public ItgBuilder addFile(File file) {
        check();
        already = true;
        uploadFile = file;
        return itgBuilder;
    }

    @Override
    public ItgBuilder addJson(Object obj) {
        check();
        already = true;
        jsonObj = obj;
        return itgBuilder;
    }

    @Override
    public ItgBuilder addContent(String content) {
        check();
        already = true;
        this.content = content;
        return itgBuilder;
    }


    public void build(Request.Builder builder, String method) {
        if (Ok.GET.equals(method)) {
            if (okParams != null) {
                VerificationUtils.url(builder, getOkParams());
            }
        } else if (Ok.POST.equals(method)) {
            if (okParams != null) {
                VerificationUtils.formBodyParams(builder, okParams);
            } else if (jsonObj != null) {
                VerificationUtils.jsonBodyJson(builder, jsonObj);
            } else if (uploadFile != null) {
                if (!uploadFile.exists()) throw new IllegalArgumentException("file does not exist");
                builder.post(RequestBody.create(MediaType.parse("application/octet-stream"), uploadFile));
            } else if (content == null || content.equals("")) {
                builder.post(RequestBody.create(MediaType.parse("application/json"), content));
            }

        }

    }

    private HashMap<String, String> getOkParams() {
        if (okParams == null) {
            okParams = new HashMap<>();
        }
        return okParams;
    }

    private void check() {
        if (already) {
            throw new IllegalArgumentException("data already load complete");
        }
    }

}
