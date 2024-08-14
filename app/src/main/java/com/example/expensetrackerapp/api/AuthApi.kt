package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/users/authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): String
}
