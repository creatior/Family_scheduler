package com.example.family_scheduler

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val selectedDate = intent.getLongExtra("SELECTED_DATE", 0)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val noteEditText: EditText = findViewById(R.id.noteEditText)
        val saveButton: Button = findViewById(R.id.saveButton)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        dateTextView.text = formattedDate

        saveButton.setOnClickListener {
            val note = noteEditText.text.toString()
            // TODO: save note
            Toast.makeText(this, "Note saved: $note", Toast.LENGTH_SHORT).show()
        }
    }
}