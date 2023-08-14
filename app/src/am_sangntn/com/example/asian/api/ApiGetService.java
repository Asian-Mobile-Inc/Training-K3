package com.example.asian.api;

import com.example.asian.model.Image;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiGetService {

    //https://api.gyazo.com/api/images?access_token=XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo
    //https://upload.gyazo.com/api/upload
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiGetService apiGetService = new Retrofit.Builder()
            .baseUrl("https://api.gyazo.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiGetService.class);

    @GET("images")
    Call<List<Image>> getImage(@Query("access_token") String access_token);
}

