package com.yk.okhttp_lib.okhttp;

import okhttp3.MediaType;

 public class ItemFile {
    private String name;
    private String fileName;
    private MediaType mediaType;
    private Object content;

     public ItemFile(String name, String fileName, MediaType mediaType, Object content) {
         this.name = name;
         this.fileName = fileName;
         this.mediaType = mediaType;
         this.content = content;
     }

     public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
