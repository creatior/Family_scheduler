package com.example.family_scheduler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val calendarView: CalendarView = findViewById(R.id.calendarView)

        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.WEEK_OF_YEAR, -2)
        val minDate = currentDate.timeInMillis
        calendarView.minDate = minDate

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = java.util.Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis
            try {
                val intent = Intent(this, ScheduleViewActivity::class.java)
                intent.putExtra("SELECTED_DATE", selectedDate)
                startActivity(intent)
            } catch(e: Exception)
            {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        val groupButton: Button = findViewById(R.id.groupButton)
        groupButton.setOnClickListener{
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }
    }
}