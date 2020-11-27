package com.plugin.androidbase.datas;

import java.util.List;

import androidx.databinding.BaseObservable;

public  class NewsData extends BaseObservable {

        private String title;
        private String from;
        private String createTime;
        private List<String> img;
        private String defaultStr="";

    public String getDefaultStr() {
        return defaultStr;
    }

    public void setDefaultStr(String defaultStr) {
        this.defaultStr = defaultStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}