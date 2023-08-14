package com.example.asian.api;

import com.example.asian.model.DeleteResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface APIDeleteService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor("XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo"))
            .build();

    APIDeleteService apiDeleteService = new Retrofit.Builder()
            .baseUrl("https://api.gyazo.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(APIDeleteService.class);

    @DELETE("images/{imageId}")
    Call<DeleteResponse> deleteImage(
            @Header("Authorization") String authorization,
            @Path("imageId") String imageId);
}
