package com.example.asian.beans;

public class User {
    private final Integer mIdUser;
    private final String mUserName;
    private final Integer mAge;

    public Integer getmIdUser() {
        return mIdUser;
    }

    public String getmUserName() {
        return mUserName;
    }

    public Integer getmAge() {
        return mAge;
    }

    public User(Integer mIdUser, String mUserName, Integer mAge) {
        this.mIdUser = mIdUser;
        this.mUserName = mUserName;
        this.mAge = mAge;
    }
}
