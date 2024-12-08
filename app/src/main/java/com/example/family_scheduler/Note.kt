package com.example.family_scheduler
import java.time.LocalDateTime

class Note (
    val start_time: String,
    val end_time: String,
    val user_id: Int,
    val note: String,
    val id: Int = -1
)