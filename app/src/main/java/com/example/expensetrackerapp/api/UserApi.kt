package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.PasswordChangeRequest
import com.example.expensetrackerapp.model.UserDetails
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("/api/users/user-details")
    suspend fun getUserDetails(): UserDetails

    @DELETE("/api/delete-user")
    suspend fun deleteUser(): Response<ResponseBody>

    @PUT("/api/users/change-password")
    suspend fun changePassword(@Body passwordChangeRequest: PasswordChangeRequest): Response<ResponseBody>
}