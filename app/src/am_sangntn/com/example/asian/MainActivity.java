package com.example.asian;

import static com.example.asian.api.ApiGetService.apiGetService;
import static com.example.asian.api.ApiPostService.apiPostService;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asian.model.Image;
import com.example.asian.model.UploadResponse;
import com.example.asian.view.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS = {Manifest.permission.READ_MEDIA_IMAGES};
    private ImageAdapter imageAdapter;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetApi = findViewById(R.id.btnGetApi);
        Button btnPostApi = findViewById(R.id.btnPost);

        btnGetApi.setOnClickListener(view -> callApi());
        btnPostApi.setOnClickListener(view -> checkPermissionsAndPickImage());
    }

    private void checkPermissionsAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        } else {
            openImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            String imagePath = getPath(this, imageUri);
            if (imagePath != null) {
                UploadImage(imagePath);
            }
        }
    }

    public void UploadImage(String filePath) {
        File imageFile = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imagedata", imageFile.getName(), requestBody);

        Call<UploadResponse> call = apiPostService.uploadImage(imagePart);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "successfully !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.d("ddd", Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    public static String getPath(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }

    //https://api.gyazo.com/api/images?access_token=XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo
    private void callApi() {
        apiGetService.getImage("XZiQLn5Xu3cjUTKYpQKOUsYHweBoxKJVOgCfneoY1Yo").enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                List<Image> listImage = response.body();
                List<String> imageUrls = new ArrayList<>();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listImage.forEach(image -> imageUrls.add(image.getUrl()));
                }
                imageAdapter = new ImageAdapter(imageUrls);

                RecyclerView recyclerView = findViewById(R.id.rcvImage);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                recyclerView.setAdapter(imageAdapter);

                Toast.makeText(MainActivity.this, "success !", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_LONG).show();
                Log.d("ddd", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}