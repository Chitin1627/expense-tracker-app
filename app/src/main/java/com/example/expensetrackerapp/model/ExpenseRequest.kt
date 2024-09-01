package com.example.expensetrackerapp.model

data class ExpenseRequest(
    val amount: Double = 0.0,
    val category_id: String = "",
    val type: String = "",
    val description: String = "",
    val date: String = "",
)