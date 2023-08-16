package com.example.asian.ex02_use_retrofit_room_image_mvvm.data.remote

import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/images")
    suspend fun getAllImages(
        @Query("access_token") accessToken: String
    ): MutableList<Image>
}
