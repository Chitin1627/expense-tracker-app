package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.UserSpending
import com.example.expensetrackerapp.model.UserSpendingRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserSpendingApi {
    @GET("/api/user-spending")
    suspend fun getUserSpending(): UserSpending

    @POST("/api/user-spending")
    suspend fun setMonthlyLimit(@Body newLimit: UserSpendingRequest): UserSpending
}