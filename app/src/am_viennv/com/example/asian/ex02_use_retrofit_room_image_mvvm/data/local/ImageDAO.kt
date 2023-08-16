package com.example.asian.ex02_use_retrofit_room_image_mvvm.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image


@Dao
interface ImageDAO {
    @Query("SELECT * FROM images")
    suspend fun getAll(): MutableList<Image>

    @Query("UPDATE images SET favorite=:favorite WHERE id = :id")
    suspend fun updateFavoriteState(favorite: Boolean, id: String)

    @Query("UPDATE images SET download=1 WHERE id = :id")
    suspend fun updateDownloadState(id: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(images: MutableList<Image>)
}
