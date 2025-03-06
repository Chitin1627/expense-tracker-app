package com.example.expensetrackerapp.model

data class Debt(
    val id: String? = null,
    val userId: String = "",
    val otherParty: String,
    val amount: Double,
    val isReceivable: Boolean,
    val createdDate: String = ""
)

