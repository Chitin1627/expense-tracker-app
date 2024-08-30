package com.example.expensetrackerapp.model

data class PasswordChangeRequest(
    val oldPassword: String = "",
    val newPassword: String = ""
)
