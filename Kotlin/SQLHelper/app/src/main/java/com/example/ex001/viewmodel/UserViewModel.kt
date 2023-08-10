package com.example.ex001.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ex001.model.User
import com.example.ex001.model.UserHelper
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userHelper = UserHelper(application)

    fun addUser(user: User) {
        viewModelScope.launch {
            userHelper.insertUser(user)
        }
    }

    fun getAllUsers(): LiveData<List<User>> {
        val usersLiveData = MutableLiveData<List<User>>()
        viewModelScope.launch {
            val users = userHelper.getAllUsers()
            usersLiveData.postValue(users)
        }
        return usersLiveData
    }

    fun deleteAllUsers(): Int {
        return userHelper.deleteAllUsers()
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            userHelper.deleteUser(userId)
        }
    }
}