package com.example.ex01.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val age: Int
)
