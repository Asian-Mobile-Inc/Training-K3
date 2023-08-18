package com.example.roomdb.model

import androidx.lifecycle.LiveData

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