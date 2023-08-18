package com.example.ex02.bean

import com.google.gson.annotations.SerializedName

data class Metadata(
    val app: String,

    val title: String,

    val url: String,

    val desc: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("original_url")
    val originalUrl: String
)