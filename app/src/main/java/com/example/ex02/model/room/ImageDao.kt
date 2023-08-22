package com.example.ex02.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ex02.bean.ItemImage

@Dao
interface ImageDao {
    @Query("SELECT * FROM images")
    fun readAllImages(): LiveData<List<ItemImage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: ItemImage)

    @Delete
    suspend fun delete(image: ItemImage)

    @Query("SELECT * FROM images WHERE imageUrl = :url")
    suspend fun getImageByUrl(url: String): ItemImage?

    @Query("DELETE FROM images WHERE imageUrl = :url ")
    suspend fun deleteImageByUrl(url: String)
}