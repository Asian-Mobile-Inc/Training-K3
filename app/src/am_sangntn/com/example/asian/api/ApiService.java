package com.example.asian.api;

import com.example.asian.model.Image;
import com.example.asian.model.UploadResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    //https://api.gyazo.com/api/images?access_token=XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo
    //https://upload.gyazo.com/api/upload
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://upload.gyazo.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("images")
    Call<List<Image>> getImage(@Query("access_token") String access_token);

    @Headers("Authorization: Bearer XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo")
    @Multipart
    @POST("api/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);
}
