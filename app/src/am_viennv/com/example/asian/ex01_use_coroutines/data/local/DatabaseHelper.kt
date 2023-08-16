package com.example.asian.ex01_use_coroutines.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.asian.ex01_use_coroutines.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "Userdata.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create Table UsersInfo(idUser INTEGER PRIMARY KEY,name TEXT, age TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, ii: Int) {
        db.execSQL("drop Table if exists UsersInfo")
    }

    suspend fun insertUserData(idUser: Int?, name: String?, age: Int?) =
        withContext(Dispatchers.IO) {
            val db = writableDatabase
            val contentValues = ContentValues()
            contentValues.put("idUser", idUser)
            contentValues.put("name", name)
            contentValues.put("age", age)
            db.insert("UsersInfo", null, contentValues)
        }

    suspend fun getAllUser(): MutableList<User> = withContext(Dispatchers.IO) {
        val users = mutableListOf<User>()
        val query = "SELECT * FROM UsersInfo"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val idUser = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getInt(2)
            users.add(User(idUser, name, age))
        }
        cursor.close()
        users
    }

    suspend fun findAllUsers(charToFind: String): MutableList<User> = withContext(Dispatchers.IO) {
        val users = mutableListOf<User>()
        val query = "SELECT * FROM UsersInfo WHERE name LIKE '%$charToFind%'"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val idUser = cursor.getInt(0)
            val name = cursor.getString(1)
            val age = cursor.getInt(2)
            users.add(User(idUser, name, age))
        }
        cursor.close()
        users
    }

    fun deleteAll(): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete("UsersInfo", null, null)
        db.close()
        return rowsDeleted
    }

    fun deleteOneUser(idUser: Int) {
        val db = this.writableDatabase
        db.delete("UsersInfo", "idUser = ?", arrayOf(idUser.toString()))
        db.close()
    }
}
