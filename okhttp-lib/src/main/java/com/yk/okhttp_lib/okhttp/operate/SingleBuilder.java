package com.yk.okhttp_lib.okhttp.operate;

import java.io.File;

public interface SingleBuilder{
    ItgBuilder addParams(String key, String params);

    ItgBuilder addFile(String file);

    ItgBuilder addFile(File file);

    ItgBuilder addJson(Object obj);

    ItgBuilder addContent(String content);

}
