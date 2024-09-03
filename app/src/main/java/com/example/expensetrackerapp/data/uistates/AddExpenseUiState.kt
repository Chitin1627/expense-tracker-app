package com.example.expensetrackerapp.data.uistates

data class AddExpenseUiState(
    val expenseId: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val type: String = "",
    val description: String = "",
    val date: String = "",
    val created_at: String = ""
)
