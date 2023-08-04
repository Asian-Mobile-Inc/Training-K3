package com.example.asian;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    private TextView tvSuccess, tvSpeed;

    private EditText mEdtURL;

    private Disposable mDisposable;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnDownload = findViewById(R.id.btnDownload);
        mImageView = findViewById(R.id.imageView);
        mEdtURL = findViewById(R.id.edtURL);
        mProgressBar = findViewById(R.id.progressBarSpeed);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvSuccess = findViewById(R.id.tvSuccess);

        btnDownload.setOnClickListener(view -> {
            Observer<Object> observer = getObserverBitmap();
            Observable<Object> observable = getObservableBitmap();

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        });
    }

    private io.reactivex.rxjava3.core.Observable<Object> getObservableBitmap() {
        return Observable.create(emitter -> {
            try {
                long startTime = SystemClock.elapsedRealtime();
                URL url = new URL(mEdtURL.getText().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                int contentLength = connection.getContentLength();

                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                int totalBytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    int progress = (int) ((totalBytesRead * 100L) / contentLength);
                    emitter.onNext(progress);
                }

                inputStream.close();
                connection.disconnect();
                long endTime = SystemClock.elapsedRealtime();
                long elapsedTime = endTime - startTime;
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                connection.disconnect();
                float speed = bitmap.getByteCount() / (elapsedTime / 1000f);
                emitter.onNext(speed);
                emitter.onNext(bitmap);
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    private Observer<Object> getObserverBitmap() {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Object data) {
                if (data instanceof Integer) {
                    int progress = (Integer) data;
                    mProgressBar.setProgress(progress);
                    tvSuccess.setText("Downloading ... " + progress + "%");


                } else if (data instanceof  Bitmap) {
                    mImageView.setImageBitmap((Bitmap) data);
                  //  mProgressBar.setProgress(100);
                    tvSuccess.setText("Downloaded");
                } else if (data instanceof Float) {
                    tvSpeed.setText("Speed: " + String.format("%.2f MB/s",(float) data / 1024 / 1024));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("ddd", e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        mDisposable.dispose();
        super.onDestroy();
    }


}