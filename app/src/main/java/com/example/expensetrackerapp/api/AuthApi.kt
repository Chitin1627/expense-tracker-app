package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.JwtToken
import com.example.expensetrackerapp.model.LoginRequest
import com.example.expensetrackerapp.model.RegisterRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/users/authenticate")
    suspend fun login(@Body loginRequest: LoginRequest): JwtToken

    @POST("/api/users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): JwtToken

    @POST("/api/users/validate-token")
    suspend fun validateToken(@Body token: JwtToken): String
}
