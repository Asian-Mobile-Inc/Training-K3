package com.example.asian;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("image_id")
    private String mIdImage;
    @SerializedName("url")
    private final String mUrl;

    public Image(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmIdImage() {
        return mIdImage;
    }

    public String getmUrl() {
        return mUrl;
    }
}
