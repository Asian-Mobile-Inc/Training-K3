package com.example.asian;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int caseDownload;
    private Button mButtonThread;
    private Button mButtonAsysn;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariable();
        handleClick();
    }

    private void initVariable() {
        mButtonThread = findViewById(R.id.btn_thread);
        mButtonAsysn = findViewById(R.id.btn_asysn);
        mImageView = findViewById(R.id.iv_image);
        progressBar = findViewById(R.id.progressBar);
    }

    private ProgressBar progressBar;

    private void handleClick() {
        mButtonThread.setOnClickListener(view -> {
            caseDownload = 0;
            startDownloadFile();
        });

        mButtonAsysn.setOnClickListener(view -> {
            caseDownload = 1;
            startDownloadFile();
        });
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }


    private void startDownloadFile() {
        if (isConnectedToInternet()) {
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please Connect the Internet !", Toast.LENGTH_SHORT).show();
            return;
        }
        String mCurrentTimeAsStr = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        String mPathFileSave = mCurrentTimeAsStr + "image_download.jpg";
        String URL_IMAGE = "https://haycafe.vn/wp-content/uploads/2022/01/hinh-anh-galaxy-vu-tru-dep.jpg";
        if (caseDownload == 0) {
            DownloadByThread downloadThread = new DownloadByThread(this, URL_IMAGE, mPathFileSave, mImageView);
            downloadThread.start();
        } else {
            DownloadImageAsyncTask downloadTask = new DownloadImageAsyncTask(this, progressBar, mImageView, mPathFileSave);
            downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, URL_IMAGE);
        }
    }

}