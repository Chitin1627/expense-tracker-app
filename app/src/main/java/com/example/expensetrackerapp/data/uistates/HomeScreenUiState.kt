package com.example.expensetrackerapp.data.uistates

import com.example.expensetrackerapp.model.CategoryExpense
import com.example.expensetrackerapp.model.Expense

data class HomeScreenUiState(
    val monthlyLimit: Double = 0.0,
    val currentMonthExpense: Double = 0.0,
    val netExpense: Double = 0.0,
    val expensesByDate: List<Expense> = emptyList(),
    val categories: HashMap<String, String> = HashMap<String, String>(),
    val categoryNameMap: HashMap<String, String> = HashMap<String, String>(),
    val expenseByCategory: HashMap<String, Double> = HashMap<String, Double>(),
    val listOfExpenseByCategory: List<CategoryExpense> = ArrayList<CategoryExpense>(),
    val totalExpense: Double = 0.0,
    val isDataLoaded: Boolean = false,
    val selectedDate: String = ""
)
