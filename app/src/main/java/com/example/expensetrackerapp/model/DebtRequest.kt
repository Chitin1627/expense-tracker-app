package com.example.expensetrackerapp.model

data class DebtRequest(
    val otherParty: String = "",
    val amount: Double = 0.0,
    val receivable: Boolean = false,
    val completed: Boolean = true,
    val description: String = "",
)
