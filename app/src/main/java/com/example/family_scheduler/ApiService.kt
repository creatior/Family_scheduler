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

    fun login(context: Context, login: String, password: String): Int
    {
        var id = -1
        try {
            db = DBHelper(context, null)
            val user = db.getUser(login, password)
            if (user == null) {
                Toast.makeText(db.context, "Invalid username/email or password", Toast.LENGTH_LONG).show()
            }
            else {
                id = user.id
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return id
    }

    fun AddNote(context: Context, startTime: String, endTime: String, user_id: Int, note: String) : Boolean
    {
        var result = false
        try {
            db = DBHelper(context, null)
            db.addNote(Note(startTime, endTime, user_id, note))
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }

    fun getNotes(context: Context, selectedDate: Long, userId: Int) : List<Note>
    {
        var result: List<Note> = emptyList()
        try {
            db = DBHelper(context, null)
            result = db.getNotesByDate(selectedDate, userId)
        } catch (e: Exception) {
            e.printStackTrace()
            result = emptyList()
        } finally {
            db.close()
        }
        return result
    }

//    fun createGroup(context: Context, name: String) : Boolean
//    {
//        var result = false
//        try {
//            db = DBHelper(context, null)
//            db.addFamily(Family(name))
//            result = true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            result = false
//        } finally {
//            db.close()
//        }
//        return result
//    }

    fun checkFamily(context: Context, userId: Int): Boolean{
        var result = false
        try {
            db = DBHelper(context, null)
            val family: Family = db.getUserFamily(userId)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
            result = false
        } finally {
            db.close()
        }
        return result
    }

    fun getUserNameByID(context: Context, user_id: Int): String
    {
        try {
            db = DBHelper(context, null)
            val user: User? = db.getUserByID(user_id.toString())
            return user!!.name
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
        return ""
    }
}
