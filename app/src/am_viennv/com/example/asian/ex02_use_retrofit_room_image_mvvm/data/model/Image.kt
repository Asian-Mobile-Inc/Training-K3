package com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
class Image(
    @PrimaryKey
    @SerializedName("image_id")
    @ColumnInfo(name = "id")
    var mIdImage: String,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    var mName: String? = null,

    @SerializedName("url")
    @ColumnInfo(name = "url")
    var mUrl: String,

    @ColumnInfo(name = "favorite")
    var mFavorite: Boolean = false,

    @ColumnInfo(name = "download")
    var mDownload: Boolean = false
)
