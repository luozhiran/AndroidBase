package com.plugin.okhttp_lib.okhttp.interceptors;

import com.itg.lib_log.L;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public class LoggerInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum HL {
        BASIC,
        HEADERS,
        BODY
    }


    private HL level;

    public LoggerInterceptor(HL hl) {
        level = hl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (level == null) {
            return chain.proceed(chain.request());
        }
        if (level == HL.BODY) {
            return printBody(chain);
        } else if (level == HL.HEADERS) {
            return chain.proceed(chain.request());
        } else if (level == HL.BASIC) {
            return printResponseBody(chain);
        }
        return chain.proceed(chain.request());
    }

    private Response printResponseBody(Chain chain) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("-------response content-------").append("\n");
        Response response = null;
        long startNs = System.nanoTime();
        response = chain.proceed(chain.request());
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        sb.append("-------response content-------").append("\n");
        sb.append("response time:").append(tookMs).append("\n");

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        sb.append("ResponseBody size: ").append(FormatFileSize(contentLength)).append("\n");
        sb.append("response code: ").append(response.code()).append("\n");
        if (response.message().length() != 0) {
            sb.append("HTTP status message: ").append(response.message()).append("\n");
        }
        sb.append(response.request().url()).append("\n");

        Headers headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            sb.append(headers.name(i)).append(":").append(headers.value(i)).append("\n");
        }

        if (HttpHeaders.hasBody(response)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);// Buffer the entire body.
            Buffer buffer = source.getBuffer();
            Long gzippedLength = null;

            if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                gzippedLength = buffer.size();
                GzipSource gzippedResponseBody = null;
                try {
                    gzippedResponseBody = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzippedResponseBody);
                } finally {
                    if (gzippedResponseBody != null) {
                        gzippedResponseBody.close();
                    }
                }
                sb.append("gzip size: ").append(gzippedLength).append("\n");
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                sb.append("ResponseBody is byte").append("\n");
            } else {
                if (contentLength != 0) {
                    sb.append("-----ResponseBody content-----").append("\n");
                    sb.append(buffer.clone().readString(charset)).append("\n");
                }
            }
        } else {
            sb.append("response don't have  responseBody").append("\n");
        }
        L.d(sb.toString());
        return response;
    }


    private Response printBody(Chain chain) throws IOException {
        Response response = null;
        Request request = chain.request();
        RequestBody body = request.body();
        Connection connection = chain.connection();
        StringBuilder sb = new StringBuilder();
        sb.append(" \n----- okhttp request log ----- \n");
        sb.append(request.method()).append("\n");
        sb.append(request.url()).append("\n");
        if (connection != null) {
            sb.append(connection.protocol().toString()).append("\n");
        }
        if (body != null) {
            sb.append("RequestBody size：").append(FormatFileSize(body.contentLength())).append("\n");
            if (body.contentType() != null) {
                sb.append("Content-Type:").append(body.contentType()).append("\n");
            }
            sb.append("Content-Length:").append(body.contentLength()).append("\n");
        }

        Headers headers = request.headers();

        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                sb.append(name).append(":").append(headers.value(i));
            }
        }
        if (body != null) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            sb.append("\n");
            sb.append("-------RequestBody content-------").append("\n");

            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset)).append("\n");
            } else {
                sb.append("requestBody is byte").append("\n");
            }
        }

        long startNs = System.nanoTime();
        response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        sb.append("-------Response Header-------").append("\n");
        sb.append("response time:").append(tookMs).append("\n");

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        sb.append("ResponseBody size: ").append(FormatFileSize(contentLength)).append("\n");
        sb.append("response code: ").append(response.code()).append("\n");
        if (response.message().length() != 0) {
            sb.append("HTTP status message: ").append(response.message()).append("\n");
        }
        sb.append(response.request().url()).append("\n");


        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            sb.append(headers.name(i)).append(":").append(headers.value(i)).append("\n");
        }

        if (HttpHeaders.hasBody(response)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);// Buffer the entire body.
            Buffer buffer = source.getBuffer();
            Long gzippedLength = null;

            if ("gzip".equalsIgnoreCase(headers.get("Content-Encoding"))) {
                gzippedLength = buffer.size();
                GzipSource gzippedResponseBody = null;
                try {
                    gzippedResponseBody = new GzipSource(buffer.clone());
                    buffer = new Buffer();
                    buffer.writeAll(gzippedResponseBody);
                } finally {
                    if (gzippedResponseBody != null) {
                        gzippedResponseBody.close();
                    }
                }
                sb.append("gzip size: ").append(gzippedLength).append("\n");
            }

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                sb.append("ResponseBody is byte").append("\n");
            } else {
                if (contentLength != 0) {
                    sb.append("-----ResponseBody content-----").append("\n");
                    sb.append(buffer.clone().readString(charset)).append("\n");
                }
            }
        } else {
            sb.append("response don't have  responseBody").append("\n");
        }
        L.d(sb.toString());
        return response;
    }


    private static String FormatFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
