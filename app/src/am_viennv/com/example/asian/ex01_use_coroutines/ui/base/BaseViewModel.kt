package com.example.asian.ex01_use_coroutines.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    private val mEmpty = MutableLiveData<Boolean>().apply { value = false }
    val mDataLoading = MutableLiveData<Boolean>().apply { value = false }
    val mToastMessage = MutableLiveData<String>()

    protected fun showLoading() {
        mDataLoading.postValue(true)
    }

    protected fun hideLoading() {
        mDataLoading.postValue(false)
    }

    protected fun showEmptyState() {
        mEmpty.postValue(true)
    }

    protected fun hideEmptyState() {
        mEmpty.postValue(false)
    }

    protected fun showToast(message: String) {
        mToastMessage.postValue(message)
    }
}
