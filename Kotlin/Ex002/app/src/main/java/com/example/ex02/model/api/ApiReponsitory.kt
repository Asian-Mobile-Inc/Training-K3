package com.example.ex02.model.api

import com.example.ex02.bean.Image

class ApiRepository(private val apiService: ApiService) {

    suspend fun getImages(token: String): List<Image> {
        return apiService.getImages("Bearer $token")
    }
}