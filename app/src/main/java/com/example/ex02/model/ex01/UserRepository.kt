package com.example.ex02.model.ex01

import androidx.lifecycle.LiveData
import com.example.ex02.model.ex01.UserDao

class UserRepository(private val userDao: UserDao) {
    val readAllUser: LiveData<List<User>> = userDao.readAllUsers()

    suspend fun addUser(user: User) {
        userDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAll()
    }
}