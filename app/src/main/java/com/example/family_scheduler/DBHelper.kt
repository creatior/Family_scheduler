package com.example.registrationtest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.family_scheduler.User

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "app", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "surname TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users;")
        onCreate(db)
    }

    fun addUser(user: User){
        val db = this.writableDatabase

        val sql = "INSERT INTO users (surname, name, email, username, password) VALUES (?, ?, ?, ?, ?)"
        val statement = db.compileStatement(sql)
        statement.bindString(1, user.surname)
        statement.bindString(2, user.name)
        statement.bindString(3, user.email)
        statement.bindString(4, user.username)
        statement.bindString(5, user.password)

        val result = statement.executeInsert()

        db.close()
    }

    fun getUser(login: String, password: String) : Boolean
    {
        val db = this.writableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE username ='$login' and password = '$password'", null)
        return result.moveToFirst()
    }

    fun selectAll(): Cursor?
    {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM users", null)
    }
}