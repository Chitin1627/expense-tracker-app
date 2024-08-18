package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ExpenseApi {
    @GET("/api/expenses/user-expenses")
    suspend fun getUserExpenses(): List<Expense>

    @POST("/api/expenses")
    suspend fun createExpense(@Body expense: ExpenseRequest): Response<Void>
}
