package com.example.ex02.model.room

import androidx.lifecycle.LiveData
import com.example.ex02.bean.ItemImage

class ImageRepository(private val imageDao: ImageDao) {
    val readAllImages: LiveData<List<ItemImage>> = imageDao.readAllImages()

    suspend fun addImage(itemImage: ItemImage) {
        imageDao.insert(itemImage)
    }

    suspend fun deleteImage(itemImage: ItemImage) {
        imageDao.delete(itemImage)
    }

    suspend fun doesImageExist(url: String): Boolean {
        return imageDao.getImageByUrl(url) != null
    }
}