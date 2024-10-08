package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.model.ExpenseRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ExpenseApi {
    @GET("/api/expenses/user-expenses")
    suspend fun getUserExpenses(): List<Expense>

    @GET("/api/expenses/user-expenses/current-month")
    suspend fun getUserExpensesCurrentMonth(): List<Expense>

    @GET("/api/expenses/user-expenses/date")
    suspend fun getUserExpenseByDate(@Query("date") date: String): ArrayList<Expense>

    @POST("/api/expenses")
    suspend fun createExpense(@Body expense: ExpenseRequest): Response<Void>

    @DELETE("/api/expenses/delete")
    suspend fun deleteExpense(@Query("id") id: String): Response<String>

    @PUT("/api/expenses/edit")
    suspend fun editExpense(@Body expense: Expense): Response<ResponseBody>

}
