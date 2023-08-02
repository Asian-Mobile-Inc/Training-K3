package com.example.asian;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButtonDownload;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        handleClick();
    }

    private void handleClick() {
        mButtonDownload.setOnClickListener(view -> {
            String url = "https://haycafe.vn/wp-content/uploads/2022/01/hinh-anh-galaxy-vu-tru-dep.jpg";
            String lastNameFile = "image.jpg";

            mDisposable = downloadImage(url, lastNameFile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(imageUrl -> {
                        if (imageUrl != null) {
                            mImageView.setImageURI(Uri.parse(imageUrl));
                            Toast.makeText(this, "Download Image Success !", Toast.LENGTH_SHORT).show();
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        Toast.makeText(this, "Download Image Fail !", Toast.LENGTH_SHORT).show();
                    });
        });
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void initView() {
        mImageView = findViewById(R.id.imageView);
        mButtonDownload = findViewById(R.id.btnDownLoad);
    }

    public Observable<String> downloadImage(String mUrl, String mPathFileSave) {
        return Observable.create(emitter -> {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    emitter.onError(new IOException("Download failed. Response code: " + responseCode));
                    return;
                }

                InputStream inputStream = connection.getInputStream();
                File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mPathFileSave);
                FileOutputStream outputStream = new FileOutputStream(imageFile);

                byte[] data = new byte[1024];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                }

                inputStream.close();
                outputStream.close();

                emitter.onNext(imageFile.getAbsolutePath());
                emitter.onComplete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }
}
