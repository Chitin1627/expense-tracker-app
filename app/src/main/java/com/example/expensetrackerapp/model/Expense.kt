package com.example.expensetrackerapp.model

import java.time.LocalDateTime
import java.util.Date

data class Expense(
    val _id: String,
    val username: String,
    val category_id: String,
    val amount: Double,
    val description: String,
    val date: String,
    val created_at: String
)
