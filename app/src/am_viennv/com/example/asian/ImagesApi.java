package com.example.asian;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImagesApi {
    @GET("api/images")
    Call<List<Image>> getAllImages(
            @Query("access_token") String accessToken
    );

    @Multipart
    @POST("api/upload")
    Call<Image> uploadImage(
            @Query("access_token") String accessToken,
            @Part MultipartBody.Part imagePart,
            @Part("title") RequestBody title
    );

    @DELETE("api/images/{image_id}")
    Call<Image> deleteImage(
            @Path("image_id") String imageId,
            @Query("access_token") String accessToken
    );
}
