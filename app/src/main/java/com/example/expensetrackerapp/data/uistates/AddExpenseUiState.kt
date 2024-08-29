package com.example.expensetrackerapp.data.uistates

data class AddExpenseUiState(
    val amount: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val date: String = ""
)
