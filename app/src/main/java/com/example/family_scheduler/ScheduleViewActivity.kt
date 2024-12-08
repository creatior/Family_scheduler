package com.example.family_scheduler

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.registrationtest.DBHelper
import java.text.SimpleDateFormat
import java.util.Locale

class ScheduleViewActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val API = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)

        val selectedDate = intent.getLongExtra("SELECTED_DATE", 0)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val addNoteButton: Button = findViewById(R.id.addNoteButton)
        val notesTable: TableLayout = findViewById(R.id.notesTable)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)
        dateTextView.text = formattedDate

        addNoteButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("SELECTED_DATE", selectedDate)
            startActivity(intent)
        }

        loadNotes(selectedDate, notesTable, userId!!.toInt())
    }

    private fun loadNotes(selectedDate: Long, notesTable: TableLayout, userId: Int) {
        val notes = API.getNotes(this, selectedDate, userId)

        try {
            for (note in notes) {
                val startTime = note.start_time.split(" ")[1]
                val endTime = note.end_time.split(" ")[1]
                val startHour = startTime.split(":")[0].toInt()
                val endHour = endTime.split(":")[0].toInt()

                val noteRow = TableRow(this)
                for (hour in 0 until 24) {
                    val cell = TextView(this)
                    cell.gravity = android.view.Gravity.CENTER // Center text
                    cell.setBackgroundResource(R.drawable.cell_border) // Add border
                    cell.setTextColor(resources.getColor(R.color.black)) // Set text color
                    cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f) // Set weight
                    if (hour in startHour until endHour) {
                        cell.text = note.note
                        cell.setBackgroundColor(resources.getColor(R.color.red))
                    }
                    noteRow.addView(cell)
                }
                notesTable.addView(noteRow)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
