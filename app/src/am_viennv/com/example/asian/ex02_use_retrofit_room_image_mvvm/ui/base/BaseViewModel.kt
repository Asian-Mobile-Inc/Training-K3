package com.example.asian.ex02_use_retrofit_room_image_mvvm.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val mEmpty = MutableLiveData<Boolean>().apply { value = false }
    val mDataLoading = MutableLiveData<Boolean>().apply { value = false }
    val mToastMessage = MutableLiveData<String>()

    fun showLoading() {
        mDataLoading.postValue(true)
    }

    fun hideLoading() {
        mDataLoading.postValue(false)
    }

    fun showEmptyState() {
        mEmpty.postValue(true)
    }

    fun hideEmptyState() {
        mEmpty.postValue(false)
    }

    fun showToast(message: String) {
        mToastMessage.postValue(message)
    }
}
