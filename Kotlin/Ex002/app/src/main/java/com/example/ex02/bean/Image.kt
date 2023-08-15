package com.example.ex02.bean

import com.google.gson.annotations.SerializedName

data class Image (
    @SerializedName("image_id")
    val imageId: String,

    @SerializedName("permalink_url")
    val permalinkUrl: String,

    val url: String,

    @SerializedName("access_policy")
    val accessPolicy: Boolean,

    val metadata: Metadata,

    val type: String,

    @SerializedName("thumb_url")
    val thumbUrl: String,

    @SerializedName("created_at")
    val createdAt: String
)