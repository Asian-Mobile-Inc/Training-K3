package com.example.roomdb.model

import androidx.lifecycle.LiveData
import com.example.ex02.bean.ItemImage
import com.example.ex02.model.room.ImageDao

class ImageRepository(private val imageDao: ImageDao) {
    val readAllImages: LiveData<List<ItemImage>> = imageDao.readAllImages()

    suspend fun addImage(itemImage: ItemImage) {
        imageDao.insert(itemImage)
    }

    suspend fun deleteImage(itemImage: ItemImage) {
        imageDao.delete(itemImage)
    }
}