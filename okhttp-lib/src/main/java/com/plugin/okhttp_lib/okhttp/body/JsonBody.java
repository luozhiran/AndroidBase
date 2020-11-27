package com.plugin.okhttp_lib.okhttp.body;


import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.Charset;

import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class JsonBody extends RequestBody {

    private static final MediaType CONTENT_TYPE = MediaType.get("application/json; charset=utf-8");

    private byte[] bytes;

    public JsonBody(Object object) {
        if (object instanceof String) {
            String json = (String) object;
            if (!getJSONType(json)) {
                throw new IllegalArgumentException("The transfer string is not JSON");
            }
            bytes = json.getBytes(Charset.forName("UTF-8"));
        } else {
            bytes = JSON.toJSONString(object).getBytes(Charset.forName("UTF-8"));
        }
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(bytes, 0, bytes.length);
    }

    @Override
    public long contentLength() throws IOException {
        return bytes.length;
    }

    public static boolean getJSONType(String str) {
        boolean result = false;
        if (str != null && str.equals("")) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true;
            } else if (str.startsWith("[") && str.endsWith("]")) {
                result = true;
            }
        }
        return result;
    }
}
