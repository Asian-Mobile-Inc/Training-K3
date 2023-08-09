package com.example.kotlin.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin.model.User
import com.example.kotlin.model.UserDao
import com.example.kotlin.model.UserDatabase
import com.example.kotlin.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData: LiveData<List<User>>

    private val userRepository: UserRepository

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        userRepository = UserRepository(userDao)
        readAllData = userRepository.readAllData
    }
    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }

}