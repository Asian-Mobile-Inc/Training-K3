package com.example.asian.model;

import com.google.gson.annotations.SerializedName;

public class MetaData {
    private String app;
    private String title;
    private String url;
    private String desc;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("original_url")
    private String originalUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
