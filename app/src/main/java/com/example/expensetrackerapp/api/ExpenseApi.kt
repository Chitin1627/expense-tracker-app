package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Expense
import retrofit2.http.GET
import retrofit2.http.Query

interface ExpenseApi {
    @GET("/api/expenses/user-expenses")
    suspend fun getUserExpenses(): List<Expense>
}
