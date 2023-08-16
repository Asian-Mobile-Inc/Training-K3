package com.example.asian.ex02_use_retrofit_room_image_mvvm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.asian.ex02_use_retrofit_room_image_mvvm.data.model.Image


@Database(entities = [Image::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDAO(): ImageDAO?

    companion object {
        private const val mDatabaseName = "images.db"
        private var mInstance: ImageDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ImageDatabase? {
            if (mInstance == null) {
                mInstance = databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java, mDatabaseName
                )
                    .build()
            }
            return mInstance
        }
    }
}
