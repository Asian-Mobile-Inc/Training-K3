package com.example.asian;

import static com.example.asian.MainActivity.getPath;
import static com.example.asian.api.ApiPostService.apiPostService;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.model.UploadResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GyazoImageUpload extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openImagePicker();

    }

    private void openImagePicker() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imagePath = getPath(this, imageUri);

            if (imagePath != null) {
                UploadImageToGyazo(imagePath);
            }
        }
    }

    public void UploadImageToGyazo(String filePath) {
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imagedata", imageFile.getName(), requestBody);

        Call<UploadResponse> call = apiPostService.uploadImage(imagePart);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("ddd", "yess");
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.d("ddd", t.getMessage());
            }
        });

    }
}
