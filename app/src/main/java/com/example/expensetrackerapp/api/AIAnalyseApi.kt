package com.example.expensetrackerapp.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface AIAnalyseApi {

    @GET("/api/ai")
    suspend fun getAIAnalysis(): Response<ResponseBody>
}