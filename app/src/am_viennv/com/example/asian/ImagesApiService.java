package com.example.asian;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ImagesApiService {
    private static final String mAccessToken = "RHB9_c_iW7ZtaZfbMSsMuWk2ze5hXwBTxuqTVDLSL5k";
    private final ImagesApi mImagesApi;

    public ImagesApiService(String mBaseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build();
        mImagesApi = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ImagesApi.class);
    }

    public Call<List<Image>> getAllImage() {
        return mImagesApi.getAllImages(mAccessToken);
    }

    public Call<Image> uploadImage(MultipartBody.Part imagePart, RequestBody title) {
        return mImagesApi.uploadImage(mAccessToken, imagePart, title);
    }

    public Call<Image> deleteImage(String imageId) {
        return mImagesApi.deleteImage(imageId, mAccessToken);
    }
}
