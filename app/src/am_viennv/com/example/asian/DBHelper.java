package com.example.asian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table UsersInfo(idUser INTEGER PRIMARY KEY,name TEXT, age TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists UsersInfo");
    }

    public Boolean insertUserData(Integer idUser, String name, Integer age) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("idUser", idUser);
        contentValues.put("name", name);
        contentValues.put("age", age);
        long result = DB.insert("UsersInfo", null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from UsersInfo", null);
    }

    public int deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("UsersInfo", null, null);
        db.close();
        return rowsDeleted;
    }

    public void deleteOneUser(Integer idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("UsersInfo", "idUser = ?", new String[]{String.valueOf(idUser)});
        db.close();
    }

}
