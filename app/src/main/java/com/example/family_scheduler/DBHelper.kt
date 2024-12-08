package com.example.registrationtest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.family_scheduler.Note
import com.example.family_scheduler.User
import java.text.SimpleDateFormat
import java.util.Locale

class DBHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "app", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "surname TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)"
        db!!.execSQL(query1)
        val query2 = "CREATE TABLE note (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "start_time DATETIME NOT NULL," +
                "end_time DATETIME NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "note TEXT NOT NULL," +
                "FOREIGN KEY(user_id) REFERENCES users(id))"
        db.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users;")
        db.execSQL("DROP TABLE IF EXISTS note;")
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

    fun getUser(login: String, password: String): User? {
        val db = this.writableDatabase
        val result = db.rawQuery(
            "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?",
            arrayOf(login, login, password)
        )

        return if (result.moveToFirst()) {
            val id = result.getInt(result.getColumnIndexOrThrow("id"))
            val surname = result.getString(result.getColumnIndexOrThrow("surname"))
            val name = result.getString(result.getColumnIndexOrThrow("name"))
            val email = result.getString(result.getColumnIndexOrThrow("email"))
            val username = result.getString(result.getColumnIndexOrThrow("username"))
            val password = result.getString(result.getColumnIndexOrThrow("password"))

            User(surname, name, email, username, password, id)
        } else {
            null
        }
    }

    fun addNote(note: Note)
    {
        val db = this.writableDatabase

        val sql = "INSERT INTO note (start_time, end_time, user_id, note) VALUES (?, ?, ?, ?)"
        val statement = db.compileStatement(sql)
        statement.bindString(1, note.start_time)
        statement.bindString(2, note.end_time)
        statement.bindString(3, note.user_id.toString())
        statement.bindString(4, note.note)

        val result = statement.executeInsert()

        db.close()
    }

    fun getNotesByDate(selectedDate: Long, userId: Int): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        val query = "SELECT * FROM note WHERE DATE(start_time) = ?"
        try {
            val cursor: Cursor = db.rawQuery(query, arrayOf(formattedDate))

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"))
                    val endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"))
                    val userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
                    val noteText = cursor.getString(cursor.getColumnIndexOrThrow("note"))

                    notes.add(Note(startTime, endTime, userId, noteText, id))
                } while (cursor.moveToNext())
            }

            cursor.close()
        } catch (e:Exception)
        {
            e.printStackTrace()
        }
        db.close()
        return notes
    }
}