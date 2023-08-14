package com.example.asian.model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("image_id")
    String imageId;

    @SerializedName("permalink_url")
    String permalinkUrl;

    String url;

    @SerializedName("access_policy")
    boolean accessPolicy;

    @SerializedName("metadata")
    MetaData metaData;

    String type;

    @SerializedName("thumb_url")
    String thumbUrl;

    @SerializedName("created_at")
    String createdAt;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
