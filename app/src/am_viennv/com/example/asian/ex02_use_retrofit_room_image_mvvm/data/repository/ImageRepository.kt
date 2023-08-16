package com.example.asian.ex02_use_retrofit_room_image_mvvm.data.repository

import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.remote.ApiService
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.Constants.Companion.ACCESS_TOKEN

class ImageRepository(private val apiService: ApiService) {

    suspend fun getAllImages(): MutableList<Image> {
        return apiService.getAllImages(ACCESS_TOKEN)
    }
}
