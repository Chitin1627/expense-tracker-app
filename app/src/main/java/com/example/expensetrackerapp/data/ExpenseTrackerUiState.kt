package com.example.expensetrackerapp.data

import com.example.expensetrackerapp.model.Category
import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense

data class ExpenseTrackerUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val token: String = "",
    val monthlyLimit: Double = 0.0,
    val currentMonthExpense: Double = 0.0,
    val isTokenValid: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val expensesByDate: List<Expense> = emptyList(),
    val categories: HashMap<String, String> = HashMap<String, String>(),
    val categoryNameMap: HashMap<String, String> = HashMap<String, String>(),
    val expenseByCategory: HashMap<String, Double> = HashMap<String, Double>(),
    val listOfExpenseByCategory: List<CategoryExpense> = ArrayList<CategoryExpense>(),
    val totalExpense: Double = 0.0
)
