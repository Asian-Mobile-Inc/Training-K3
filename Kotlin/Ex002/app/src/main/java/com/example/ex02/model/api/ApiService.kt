package com.example.ex02.model.api

import com.example.ex02.bean.Image
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("images")
    suspend fun getImages(@Header("Authorization") token: String): List<Image>
}