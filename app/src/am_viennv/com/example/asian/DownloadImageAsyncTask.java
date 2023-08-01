package com.example.asian;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asian.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    @SuppressLint("StaticFieldLeak")
    private final ProgressBar mProgressBar;
    @SuppressLint("StaticFieldLeak")
    private final ImageView mImageView;
    private ProgressDialog mProgressDialog;
    private final String mPathFileSave;

    public DownloadImageAsyncTask(Context context, ProgressBar progressBar, ImageView imageView, String pathFileSave) {
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mImageView = imageView;
        this.mPathFileSave = pathFileSave;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Downloading image...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressNumberFormat("%1d kb of %2d kb");
        mProgressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                Toast.makeText(mContext, "Please connect the Internet !", Toast.LENGTH_SHORT).show();
            }

            int lengthOfFile = connection.getContentLength();
            InputStream inputStream = connection.getInputStream();
            File imageFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/" + mPathFileSave);
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] data = new byte[1024];
            long total = 0;
            int count;
            mProgressDialog.setMax(lengthOfFile / 1024);
            int progress;
            while ((count = inputStream.read(data)) != -1) {
                total += count;
                progress = (int) (total / 1024);
                publishProgress(progress);
                outputStream.write(data, 0, count);
            }

            inputStream.close();
            outputStream.close();

            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Utils.showToast("Download Success !", mContext);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showToast("Download Fail !", mContext);
        }
        return bitmap;
    }

    protected void onProgressUpdate(Integer... progress) {
        mProgressBar.setProgress(progress[0]);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();
        if (result != null) {
            mImageView.setImageBitmap(result);
        }
    }

}
