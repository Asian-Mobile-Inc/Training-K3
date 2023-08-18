package com.example.ex02.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ex02.bean.ItemImage
import com.example.ex02.model.room.ImageDatabase
import com.example.ex02.model.room.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageRoomViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllImage: LiveData<List<ItemImage>>
    private val repository: ImageRepository

    init {
        val imageDao = ImageDatabase.getDatabase(application).imageDao()
        repository = ImageRepository(imageDao)
        readAllImage = repository.readAllImages
    }

    private fun addImage(itemImage: ItemImage) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addImage(itemImage)
        }
    }

    fun deleteImage(itemImage: ItemImage) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImage(itemImage)
        }
    }

    fun getAllImage(): LiveData<List<ItemImage>> {
        return readAllImage
    }

    private suspend fun checkImageExistence(url: String): Boolean {
        return repository.doesImageExist(url)
    }

    fun addItemIfNotExists(image: ItemImage) {
        viewModelScope.launch {
            if (!checkImageExistence(image.imageUrl)) {
                addImage(image)
            }
        }
    }
}