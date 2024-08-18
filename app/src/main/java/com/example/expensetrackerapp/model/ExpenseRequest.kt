package com.example.expensetrackerapp.model

import java.util.Date

data class ExpenseRequest(
    val username: String,
    val amount: Double,
    val category_id: String,
    val description: String,
    val date: String,
)
