package com.example.expensetrackerapp.data.uistates

import com.example.expensetrackerapp.model.Debt

data class DebtScreenUiState(
    val debts: List<Debt> = emptyList(),
    val totalOwed: Double = 0.0,
    val totalReceivable: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
