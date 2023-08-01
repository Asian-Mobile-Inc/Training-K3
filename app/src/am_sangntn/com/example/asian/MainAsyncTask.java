package com.example.asian;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainAsyncTask extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mTvSpeed, mTvSuccess;
    private ProgressBar mProgressBarSpeed;
    private long mStartTime;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            showToast(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_asynctask);

        mImageView = findViewById(R.id.imageView);
        mTvSpeed = findViewById(R.id.tvSpeed);
        EditText edtURL = findViewById(R.id.edtURL);
        mTvSuccess = findViewById(R.id.tvSuccess);

        mProgressBarSpeed = findViewById(R.id.progressBarSpeed);

        Button btnDownload = findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(view -> {
            String imageUrl = edtURL.getText().toString();
            if (isImageURL(imageUrl)) {
                mStartTime = SystemClock.elapsedRealtime();
                new DownloadImageTask().execute(imageUrl);
            } else {
                showToast("Failed to download image");
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Message message = mHandler.obtainMessage(1, "Started to download image");
                mHandler.sendMessage(message);

                InputStream inputStream = connection.getInputStream();
                int fileLength = connection.getContentLength();
                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    int progress = (int) ((total * 100) / fileLength);
                    publishProgress(progress);
                }
                inputStream.close();
                connection.disconnect();

                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                connection.disconnect();

                return bitmap;
            } catch (Exception e) {
                Message message = mHandler.obtainMessage(1, "Failed to download image");
                mHandler.sendMessage(message);
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                if (result != null) {
                    mImageView.setImageBitmap(result);
                    long endTime = SystemClock.elapsedRealtime();
                    long elapsedTime = endTime - mStartTime;
                    int speed = (int) (result.getByteCount() / (elapsedTime / 1000.0f));
                    mTvSpeed.setText("Tốc độ tải xuống: " + speed / (1024 * 1024.0 / 2) + " mb/s");
                    mProgressBarSpeed.setProgress(100);
                    Message message = mHandler.obtainMessage(1, "Finished to download image");
                    mHandler.sendMessage(message);

                }
            } catch (Exception e) {
                Log.d("ddd", e.toString());
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            mTvSuccess.setText("Tiến trình: " + progress + "%");
            mProgressBarSpeed.setProgress(progress);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isImageURL(String urlString) {
        try {
            String fileExtension = urlString.substring(urlString.lastIndexOf('.') + 1);
            return fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")
                    || fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("gif")
                    || fileExtension.equalsIgnoreCase("bmp");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
