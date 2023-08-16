package com.example.asian.ex02_use_retrofit_room_image_mvvm.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.asian.R
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import java.io.File

class DownloadImageUtils(
    private var context: Context,
    private var activity: Activity
) {
    fun handleDownloadImg(image: Image) {
        if (NetworkUtils.isOnline(context)) {
            if (image.mDownload) {
                showDialog(image)
            } else {
                downloadImage(image)
            }
        } else {
            activity.runOnUiThread {
                Toast.makeText(
                    context,
                    "Please connect the internet !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun downloadImage(image: Image) {
        showLoading()
        val directory = File(Environment.DIRECTORY_DOWNLOADS)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val url = image.mUrl
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val fileName: String = url.substring(url.lastIndexOf("/") + 1)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    fileName
                )
        }
        val downloadId = downloadManager.enqueue(request)

        val downloadCompleteReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context == null || intent == null) return

                if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                    val receivedDownloadId =
                        intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (receivedDownloadId == downloadId) {
                        hideLoading()
                        context.unregisterReceiver(this)
                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        context.registerReceiver(
            downloadCompleteReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private fun showLoading() {
        val mProgressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
        mProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        val mProgressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
        mProgressBar.visibility = View.GONE
    }

    private fun showDialog(image: Image) {
        val dialogClickListener =
            DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        downloadImage(image)
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure you want re-download image ?")
            .setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)
            .show()
    }
}
