package com.example.asian.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {

//    "image_id" : "8980c52421e452ac3355ca3e5cfe7a0c",
//            "permalink_url": "http://gyazo.com/8980c52421e452ac3355ca3e5cfe7a0c",
//            "thumb_url" : "https://i.gyazo.com/thumb/180/afaiefnaf.png",
//            "url" : "https://i.gyazo.com/8980c52421e452ac3355ca3e5cfe7a0c.png",
//            "type": "png"
    @SerializedName("image_id")
    String imageId;

    @SerializedName("permalink_url")
    String permalinkUrl;

    @SerializedName("thumb_url")
    String thumbUrl;

    String url;

    String type;
}
