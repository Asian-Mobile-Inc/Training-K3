package com.example.asian;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {User.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "user.db";

    private static UserDatabase mInstance;

    public static synchronized UserDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public abstract UserDao userDao();
}
