package com.example.ex02.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ex02.bean.Image
import com.example.ex02.model.api.ApiRepository
import kotlinx.coroutines.launch


class ImageApiViewModel(private val repository: ApiRepository) : ViewModel() {
    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> = _images

    fun fetchImages(token: String) {
        viewModelScope.launch {
            try {
                val imagesList = repository.getImages(token)
                _images.value = imagesList
            } catch (e: Exception) {
                Log.d("ddd", e.toString())
            }
        }
    }
}