package com.yk.okhttp_lib.okhttp.operate;

import java.io.File;

import okhttp3.MediaType;

public interface MultiBuilder{

    ItgBuilder addMultiFile(String name, String file);

    ItgBuilder addMultiFile(String name, File file);

    ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, File file);

    ItgBuilder addMultiFile(String name, MediaType mediaType, String fileName, String file);

    ItgBuilder addMultiParams(String key,String param);

    ItgBuilder addMultiJson(String key,Object json);



}
