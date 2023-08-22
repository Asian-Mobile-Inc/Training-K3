package com.example.asian.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.asian.beans.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE user_name LIKE '%' || :charToFind || '%'")
    List<User> findUserByName(String charToFind);

    @Query("DELETE FROM user")
    void deleteAll();

    @Delete
    void delete(User user);
}
