package com.example.ex02.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ex02.bean.ItemImage
import com.example.roomdb.model.ImageDatabase
import com.example.roomdb.model.ImageRepository
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

    fun addImage(itemImage: ItemImage) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addImage(itemImage)
        }
    }

    fun deleteImage(itemImage: ItemImage) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.deleteImage(itemImage)
        }
    }

    fun getAllImage(): LiveData<List<ItemImage>> {
        return readAllImage
    }
}