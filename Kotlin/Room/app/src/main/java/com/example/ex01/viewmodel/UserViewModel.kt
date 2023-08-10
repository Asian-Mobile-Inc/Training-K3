package com.example.ex01.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ex01.model.User
import com.example.ex01.model.UserDatabase
import com.example.ex01.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val readAllUser: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllUser = repository.readAllUser
    }

    fun addUser(user: User) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.addUser(user)
        }
    }
}