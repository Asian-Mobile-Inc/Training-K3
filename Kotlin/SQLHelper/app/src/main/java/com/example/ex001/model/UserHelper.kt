package com.example.ex001.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserHelper(context: Context) : SQLiteOpenHelper(context, "mydb", null, 1) {

    private var sqlCreateTable = "CREATE TABLE users (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    name VARCHAR(255),\n" +
            "    age INT\n" +
            ");"
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(sqlCreateTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", user.name)
            put("age", user.age)
        }
        db.insert("users", null, contentValues)
    }

    suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val cursor = db.query("users", null, null, null, null, null, null)
        val users = mutableListOf<User>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val age = getInt(getColumnIndexOrThrow("age"))
                users.add(User(id, name, age))
            }
        }

        users
    }

    fun deleteAllUsers(): Int {
        val db = this.writableDatabase
        val rowsDeleted = db.delete("users", null, null)
        db.close()
        return rowsDeleted
    }

    suspend fun deleteUser(userId: Int): Int = withContext(Dispatchers.IO) {
        val db = writableDatabase
        db.delete("users", "id=?", arrayOf(userId.toString()))
    }
}