package com.example.asian.ex01_use_coroutines.ui.component.launch

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.ex01_use_coroutines.data.local.DatabaseHelper
import com.example.asian.ex01_use_coroutines.data.model.User
import com.example.asian.ex01_use_coroutines.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(context: Context?) : BaseViewModel() {

    private val mDatabaseHelper = DatabaseHelper(context)

    private val mUsersLiveData = MutableLiveData<MutableList<User>>()

    fun insertUserData(name: String?, age: String?) {
        showLoading()
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    mDatabaseHelper.insertUserData(findNewId(), name, age?.toInt())
                    refreshUsersList()
                }
                showToast("Insertion Success !")
            } catch (e: Exception) {
                showToast("Insertion failed !")
            } finally {
                hideLoading()
            }
        }
    }

    private fun findNewId(): Int {
        val mData = mUsersLiveData.value
        if (mData?.isEmpty()!!) {
            return 0
        }
        mData.sortedWith(compareBy { it.mIdUser })
        for (i in 0 until mData.size) {
            if (i != mData[i].mIdUser) {
                return i
            }
        }
        return mData.size
    }

    fun getAllUsers(): LiveData<MutableList<User>> {
        viewModelScope.launch {
            refreshUsersList()
        }
        return mUsersLiveData
    }

    fun findAllUsers(charToFind: String): LiveData<MutableList<User>> {
        viewModelScope.launch {
            val tempUsers = mDatabaseHelper.findAllUsers(charToFind)
            mUsersLiveData.postValue(tempUsers)
        }
        return mUsersLiveData
    }

    fun deleteAllUsers() {
        try {
            viewModelScope.launch {
                mDatabaseHelper.deleteAll()
                refreshUsersList()
            }
            showToast("Delete Success")
        } catch (e: Exception) {
            showToast("Delete Error")
            showEmptyState()
        } finally {
            hideLoading()
        }
    }

    fun deleteOneUser(idUser: Int) {
        viewModelScope.launch {
            mDatabaseHelper.deleteOneUser(idUser)
            refreshUsersList()
        }
    }

    private suspend fun refreshUsersList() {
        try {
            showLoading()
            val tempUsers = withContext(Dispatchers.IO) {
                mDatabaseHelper.getAllUser()
            }
            hideEmptyState()
            mUsersLiveData.postValue(tempUsers)
        } catch (e: Exception) {
            showToast("Error fetching data")
            showEmptyState()
        } finally {
            hideLoading()
        }
    }
}
