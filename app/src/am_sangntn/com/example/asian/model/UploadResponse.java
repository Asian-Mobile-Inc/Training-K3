package com.example.asian.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("image_id")
    String imageId;

    @SerializedName("permalink_url")
    String permalinkUrl;

    @SerializedName("thumb_url")
    String thumbUrl;

    String url;

    String type;
}
