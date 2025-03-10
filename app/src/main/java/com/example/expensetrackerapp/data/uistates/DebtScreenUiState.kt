package com.example.expensetrackerapp.data.uistates

import com.example.expensetrackerapp.model.Debt

data class DebtScreenUiState(
    val debts: ArrayList<Debt> = ArrayList(),
    val editedDebt: Debt = Debt(),
    val totalOwed: Double = 0.0,
    val totalReceivable: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
