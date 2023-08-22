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
import kotlinx.coroutines.runBlocking

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

    suspend fun checkImageExistence(url: String): Boolean {
        return repository.doesImageExist(url)
    }

    /*fun addItemIfNotExists(image: ItemImage): Boolean {
        var check = true
        viewModelScope.launch {
            if (!checkImageExistence(image.imageUrl)) {
                addImage(image)
            } else {
                repository.deleteImageByUrl(image.imageUrl)
                check = false;
            }
        }
        return check
    }*/

    fun addItemIfNotExists(image: ItemImage): Boolean {
        return runBlocking {
            var check = true
            if (!checkImageExistence(image.imageUrl)) {
                addImage(image)
            } else {
                repository.deleteImageByUrl(image.imageUrl)
                check = false
            }
            check
        }
    }

    fun checkExistence(url: String): Boolean {
        return runBlocking {
            repository.doesImageExist(url)
        }
    }

    /*fun checkExistence(url: String): Boolean {
        var check = true
        viewModelScope.launch {
            val deferredValue: Deferred<Boolean> = async {
                repository.doesImageExist(url)
            }
            check = deferredValue.await()
        }
        return check
    }*/
}