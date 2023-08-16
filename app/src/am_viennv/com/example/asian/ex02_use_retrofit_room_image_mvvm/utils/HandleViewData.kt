package com.example.asian.ex02_use_retrofit_room_image_mvvm.utils

import android.app.Activity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import com.example.asian.R
import com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.launch.ImageViewModel

class HandleViewData(
    private val activity: Activity,
    private val mViewModel: ImageViewModel,
    private val lifecycleOwner: LifecycleOwner
) {
    private var mWindowInsetsController: WindowInsetsControllerCompat

    init {
        val window = activity.window
        mWindowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
    }

    internal fun showErrorState() {
        mViewModel.mToastMessage.observe(lifecycleOwner) { message ->
            message?.let {
                makeToast(it)
            }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(activity.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    internal fun showToolBar() {
        val toolbar = activity.findViewById<Toolbar>(R.id.tbApp)
        toolbar.visibility = View.VISIBLE
        mWindowInsetsController.show(WindowInsetsCompat.Type.systemBars())
    }

    internal fun progessloadingData() {
        val mProgressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
        mViewModel.mDataLoading.observe(lifecycleOwner) { isLoading ->
            if (isLoading) {
                mProgressBar.visibility = View.VISIBLE
            } else {
                mProgressBar.visibility = View.GONE
            }
        }
    }
}
