package com.example.expensetrackerapp.data

import com.example.expensetrackerapp.model.Expense

data class ExpenseTrackerUiState(
    val username: String = "",
    val password: String = "",
    val expenses: List<Expense> = emptyList(),
    val totalExpense: Double = 0.0
)
