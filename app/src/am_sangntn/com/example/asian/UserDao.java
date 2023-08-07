package com.example.asian;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user_table")
    List<User> getListUser();

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Delete
    void deleteUser(User user);

}
