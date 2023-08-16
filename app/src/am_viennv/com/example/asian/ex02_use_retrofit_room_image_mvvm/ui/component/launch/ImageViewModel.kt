package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.component.launch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.local.ImageDatabase
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.repository.ImageRepository
import com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.base.BaseViewModel
import com.example.asian.ex02_use_retrofit_room_image_mvvm.utils.DownloadImageUtils
import kotlinx.coroutines.launch

class ImageViewModel(
    private val mRepository: ImageRepository,
    private val mImageDatabase: ImageDatabase,
    private val mDownloadImageUtils: DownloadImageUtils
) : BaseViewModel() {

    private val mImagesMutable = MutableLiveData<MutableList<Image>>()
    val mImages: LiveData<MutableList<Image>> = mImagesMutable

    fun loadAllImagesFromAPI() {
        viewModelScope.launch {
            try {
                showLoading()
                val allImagesResponse = mRepository.getAllImages()
                mImageDatabase.imageDAO()?.insertAll(allImagesResponse)
                getAllImageFromRoom()
            } catch (e: Exception) {
                showToast("Fetch Image from API Fail !")
                Log.e("API Response", "Error: $e")
                showEmptyState()
            } finally {
                hideLoading()
            }
        }
    }

    fun getAllImageFromRoom() {
        viewModelScope.launch {
            try {
                showLoading()
                mImagesMutable.value = mImageDatabase.imageDAO()?.getAll()
                hideEmptyState()
            } catch (e: Exception) {
                showToast("Fetch Image from RoomDB Fail !")
                showEmptyState()
            } finally {
                hideLoading()
            }
        }
    }

    fun updateFavoriteState(favorite: Boolean, id: String) {
        viewModelScope.launch {
            mImageDatabase.imageDAO()?.updateFavoriteState(favorite, id)
        }
    }

    fun downloadImage(image: Image) {
        viewModelScope.launch {
            mDownloadImageUtils.handleDownloadImg(image)
            mImageDatabase.imageDAO()?.updateDownloadState(image.mIdImage)
        }
    }
}
