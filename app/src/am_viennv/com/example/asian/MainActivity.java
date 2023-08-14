package com.example.asian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.asian.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImagesApiService mImagesApiService;
    private ImageAdapter mImageAdapter;
    private ActivityMainBinding mActivityMainBinding;
    private List<Image> mImages;
    private RequestBody mTitleRequestBody;
    private MultipartBody.Part mImagePart;
    private Image mImageDelete;

    enum CaseAction {
        DELETE, UPLOAD
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        initVariables();
        initRecyclerView();
        initCallApi();
        setupClickListeners();
    }

    private void initVariables() {
        mImages = new ArrayList<>();
        mImageAdapter = new ImageAdapter(this::deleteImage);
    }

    private void deleteImage(Image image) {
        mImagesApiService = new ImagesApiService("https://api.gyazo.com/");
        mImageDelete = image;
        handleProcessData(CaseAction.DELETE);
    }

    private void handleDelete(Image imageDelete) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImages.removeIf(image -> image.getmIdImage().equals(imageDelete.getmIdImage()));
        }
        mImageAdapter.setData(mImages);
    }

    private void setupClickListeners() {
        mActivityMainBinding.flbUpload.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                uploadImage(selectedImageUri);
            }
        }
    });

    @SuppressLint("CheckResult")
    private void uploadImage(Uri imageUri) {
        String mBaseUrlUpload = "https://upload.gyazo.com/";
        mImagesApiService = new ImagesApiService(mBaseUrlUpload);
        String imagePath = createTempFileFromUri(imageUri);
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("multipart/form-data"));
            mImagePart = MultipartBody.Part.createFormData("imagedata", imageFile.getName(), requestBody);
            String imageName = getFileNameFromUri(imageUri);
            mTitleRequestBody = RequestBody.create(imageName, MediaType.parse("text/plain"));
            handleProcessData(CaseAction.UPLOAD);
        }
    }

    private void handleProcessData(CaseAction caseAction) {
        Call<Image> call;
        if (caseAction == CaseAction.UPLOAD) {
            call = mImagesApiService.uploadImage(mImagePart, mTitleRequestBody);
        } else {
            call = mImagesApiService.deleteImage(mImageDelete.getmIdImage());
        }
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(@NonNull Call<Image> call, @NonNull Response<Image> response) {
                Toast.makeText(getBaseContext(), caseAction.toString() + " success !", Toast.LENGTH_SHORT).show();
                if (caseAction == CaseAction.UPLOAD) {
                    Image imageUpload = response.body();
                    handleUploadImage(imageUpload);
                } else {
                    handleDelete(mImageDelete);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {
                Log.d("DEBUG", t.toString());
            }
        });
    }

    private void handleUploadImage(Image imageUpload) {
        mImages.add(0, imageUpload);
        mImageAdapter.setData(mImages);
    }

    private String createTempFileFromUri(Uri uri) {
        String imageName = getFileNameFromUri(uri);
        try {
            File tempFile = new File(getCacheDir(), imageName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                FileOutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
                inputStream.close();
                return tempFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initRecyclerView() {
        mActivityMainBinding.rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mActivityMainBinding.rvImage.setAdapter(mImageAdapter);
        mImageAdapter.setData(mImages);
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String displayName = null;
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (displayName == null) {
            displayName = uri.getLastPathSegment();
        }
        return displayName;
    }

    private void initCallApi() {
        String mBaseUrl = "https://api.gyazo.com/";
        mImagesApiService = new ImagesApiService(mBaseUrl);
        Call<List<Image>> call = mImagesApiService.getAllImage();
        call.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(@NonNull Call<List<Image>> call, @NonNull Response<List<Image>> response) {
                if (response.isSuccessful()) {
                    mImages = response.body();
                    mImageAdapter.setData(mImages);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Image>> call, @NonNull Throwable t) {
                Log.e("DEBUG", "Get all onFailure: " + t);
                t.printStackTrace();
            }
        });
    }
}
