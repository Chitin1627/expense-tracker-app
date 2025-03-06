package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Debt
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DebtApi {
    @POST("/api/debt/add")
    suspend fun addDebt(@Body debt: Debt): Response<Debt>

    @GET("/api/debt/get")
    suspend fun getDebtsByUser(): Response<List<Debt>>
}