package com.example.ex02.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ex02.bean.Image
import com.example.ex02.model.api.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ImageApiViewModel(private val repository: ApiRepository) : ViewModel() {
    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> = _images

    private val _imageLiveData = MutableLiveData<Bitmap>()
    val imageLiveData: LiveData<Bitmap> = _imageLiveData


    fun fetchImages(token: String) {
        viewModelScope.launch {
            val imagesList = repository.getImages(token)
            _images.value = imagesList
        }
    }

    fun loadImageFromNetwork(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = URL(url).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                _imageLiveData.postValue(bitmap)
            } catch (e: IOException) {
                Log.d("ddd", e.toString())
            }
        }
        
    }
}