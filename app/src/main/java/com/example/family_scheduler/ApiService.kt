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
            if (!is_registered) {
                Toast.makeText(db.context, "Invalid username/email or password", Toast.LENGTH_LONG).show()
                result = false
            }
            else {
                result = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }
}
