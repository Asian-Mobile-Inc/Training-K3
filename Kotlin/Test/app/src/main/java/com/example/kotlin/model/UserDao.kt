package com.example.kotlin.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    /*@Insert()
    suspend fun insertUser(user: User): Int*/
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>
}