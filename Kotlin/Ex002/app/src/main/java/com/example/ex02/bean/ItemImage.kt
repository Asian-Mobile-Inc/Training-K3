package com.example.ex02.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ItemImage (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    val imageUrl: String,

    val name: String
)