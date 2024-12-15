package com.example.family_scheduler

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.selects.select
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val API = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        val selectedDate = intent.getLongExtra("SELECTED_DATE", 0)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val noteEditText: EditText = findViewById(R.id.noteEditText)
        val saveButton: Button = findViewById(R.id.saveButton)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)

        val startTimePicker: TimePicker = findViewById(R.id.startTimePicker)
        val endTimePicker: TimePicker = findViewById(R.id.endTimePicker)

        startTimePicker.setIs24HourView(true)
        endTimePicker.setIs24HourView(true)

        startTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            view.minute = 0 // Устанавливаем минуты на 0 при изменении времени
        }

        endTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            view.minute = 0 // Устанавливаем минуты на 0 при изменении времени
        }

        dateTextView.text = formattedDate

        saveButton.setOnClickListener {
            if (startTimePicker.hour >= endTimePicker.hour) {
                Toast.makeText(this, "End time must be greater than start time", Toast.LENGTH_SHORT).show()
            }
            else {
                val note = noteEditText.text.toString()

                val startTime = FormatDate(selectedDate, startTimePicker)
                val endTime = FormatDate(selectedDate, endTimePicker)

                val isAdded = API.AddNote(this, startTime, endTime, userId!!.toInt(), note)
                if (isAdded) {
                    Toast.makeText(this, "Note saved: $note", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun FormatDate(selectedDate: Long, timePicker: TimePicker):String{
        val instant = Instant.ofEpochMilli(selectedDate)
        val zoneId = ZoneId.systemDefault()
        val selectedLocalDate = instant.atZone(zoneId).toLocalDate()

        val hour = timePicker.hour
        val minute = timePicker.minute

        val time = LocalTime.of(hour, minute)

        val dateTime = LocalDateTime.of(selectedLocalDate, time)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dateTime.format(formatter)
    }
}