package com.example.ex01.model

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val readAllUser: LiveData<List<User>> = userDao.realAllUsers()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }
}