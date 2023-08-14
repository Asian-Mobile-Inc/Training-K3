package com.example.asian.api;

import com.example.asian.model.UploadResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiPostService {

    //https://api.gyazo.com/api/images?access_token=XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo
    //https://upload.gyazo.com/api/upload
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor("XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo"))
            .build();

    ApiPostService apiPostService = new Retrofit.Builder()
            .baseUrl("https://upload.gyazo.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiPostService.class);

    @Multipart
    @POST("api/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);
}
