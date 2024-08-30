package com.example.expensetrackerapp.data.uistates

data class AuthenticationUiState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val token: String = "",
    val isTokenValid: Boolean = false,
    val isDataLoaded: Boolean = false
)
