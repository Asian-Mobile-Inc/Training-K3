package com.example.asian;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asian.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadByThread extends Thread {

    private final String mUrl;
    private final String mPathFileSave;
    private final Context mContext;
    private final ImageView mImageView;
    private File mImageFile;

    DownloadByThread(Context context, String url, String pathFileSave, ImageView imageView) {
        this.mUrl = url;
        this.mPathFileSave = pathFileSave;
        mContext = context;
        mImageView = imageView;
    }

    @Override
    public void run() {
        downloadImage(mUrl, mPathFileSave);
    }

    public void downloadImage(String mUrl, String mPathFileSave) {

        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Toast.makeText(mContext, "Please connect the Internet !", Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream inputStream = connection.getInputStream();
            mImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + mPathFileSave);
            FileOutputStream outputStream = new FileOutputStream(mImageFile);

            byte[] data = new byte[1024];
            int count;
            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }

            Utils.showToast("Download Image Success !", mContext);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> mImageView.setImageURI(Uri.parse(mImageFile.getAbsolutePath())));

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.showToast("Download Image Fail !", mContext);
        }
    }
}
