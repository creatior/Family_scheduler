package com.example.family_scheduler

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.registrationtest.DBHelper
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ScheduleViewActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val API = ApiService()
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_view)

        val selectedDate = intent.getLongExtra("SELECTED_DATE", System.currentTimeMillis())
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null)!!.toInt()

        val selectedDateTextView: TextView = findViewById(R.id.selectedDateTextView)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate)
        selectedDateTextView.text = formattedDate

        val tableLayout: TableLayout = findViewById(R.id.tableLayout)
        populateTable(tableLayout, selectedDate, userId)

        val addNoteButton: Button = findViewById(R.id.addNoteButton)
        addNoteButton.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            intent.putExtra("SELECTED_DATE", selectedDate)
            startActivity(intent)
        }
    }


    private fun populateTable(tableLayout: TableLayout, selectedDate: Long, userId: Int) {
        val notes = API.getNotes(this, selectedDate, userId)
        val userName = API.getUserNameByID(this, userId)

        val headerRow = TableRow(this)
        headerRow.addView(createCell(""))
        for (i in 0 until 24) {
            headerRow.addView(createCell("$i:00"))
        }
        tableLayout.addView(headerRow)

        val row = TableRow(this)
        row.addView(createCell(userName))
        for (i in 0 until 24) {
            val cell = createCell("")
            row.addView(cell)
        }
        tableLayout.addView(row)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        for (note in notes) {
            val startTime = LocalDateTime.parse(note.start_time, formatter)
            val endTime = LocalDateTime.parse(note.end_time, formatter)

            val startHour = startTime.hour
            val endHour = endTime.hour

            for (hour in startHour until endHour) {
                val cell = row.getChildAt(hour + 1) as TextView // +1 to skip the username cell
                val backgroundColor = getColor(R.color.red)
                val borderDrawable = resources.getDrawable(R.drawable.cell_border, theme)

                val layers = arrayOf(
                    ColorDrawable(backgroundColor),
                    borderDrawable
                )
                val layerDrawable = LayerDrawable(layers)
                cell.background = layerDrawable
                cell.setText(note.note)
                cell.setOnClickListener { showNoteDetails(note) }
            }
        }
    }



    private fun createCell(text: String): TextView {
        val cell = TextView(this)
        cell.text = text
        cell.setPadding(8, 8, 8, 8)
        cell.setBackgroundResource(R.drawable.cell_border)
        return cell
    }

    private fun showNoteDetails(note: Note) {

    }
}

