package com.example.family_scheduler

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.registrationtest.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiService {
    private lateinit var db: DBHelper

    fun register(context: Context, surname: String, name: String, email: String, username: String, password: String): Boolean {
        var result = false
        try {
            db = DBHelper(context, null)
            db.addUser(User(surname, name, email, username, password))
            Toast.makeText(db.context, "Success!", Toast.LENGTH_LONG).show()
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }

    fun login(context: Context, login: String, password: String): Boolean
    {
        var result = false
        try {
            db = DBHelper(context, null)
            val is_registered = db.getUser(login, password)
            if (is_registered) {
                Toast.makeText(db.context, "Success!", Toast.LENGTH_LONG).show()
            }
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }

    fun selectAll(context: Context): String
    {
        db = DBHelper(context, null)
        val cursor = db.selectAll()
        var result = ""
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val surname = cursor.getString(cursor.getColumnIndexOrThrow("surname"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    result += "$id\t$surname\t$name\t$username\t$email\t$password\n"
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return result
    }
}
