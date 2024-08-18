package com.example.expensetrackerapp.data

import com.example.expensetrackerapp.model.Category
import com.example.expensetrackerapp.model.Expense

data class ExpenseTrackerUiState(
    val username: String = "",
    val password: String = "",
    val token: String = "",
    val isTokenValid: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val categories: HashMap<String, String> = HashMap<String, String>(),
    val totalExpense: Double = 0.0
)
