package com.example.expensetrackerapp.model



data class Expense(
    val _id: String,
    val username: String,
    val category_id: String,
    val type: String,
    val amount: Double,
    val description: String,
    val date: String,
    val created_at: String
)
