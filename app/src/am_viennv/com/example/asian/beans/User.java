package com.example.asian.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey()
    private int mIdUser;

    @ColumnInfo(name = "user_name")
    private final String mUserName;

    @ColumnInfo(name = "age")
    private final Integer mAge;

    public int getMIdUser() {
        return mIdUser;
    }

    public String getMUserName() {
        return mUserName;
    }

    public Integer getMAge() {
        return mAge;
    }

    public User(int mIdUser, String mUserName, Integer mAge) {
        this.mIdUser = mIdUser;
        this.mUserName = mUserName;
        this.mAge = mAge;
    }
}
