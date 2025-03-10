package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Debt
import com.example.expensetrackerapp.model.DebtRequest
import com.example.expensetrackerapp.model.Expense
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface DebtApi {
    @POST("/api/debt/add")
    suspend fun addDebt(@Body debt: DebtRequest): Response<Debt>

    @GET("/api/debt/get")
    suspend fun getDebtsByUser(): Response<ArrayList<Debt>>

    @DELETE("/api/debt/delete")
    suspend fun deleteDebt(@Query("id") id: String): Response<String>

    @PUT("/api/debt/edit")
    suspend fun editDebt(@Body debt: Debt): Response<ResponseBody>
}