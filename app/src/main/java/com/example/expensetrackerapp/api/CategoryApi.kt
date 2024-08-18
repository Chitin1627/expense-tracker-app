package com.example.expensetrackerapp.api

import com.example.expensetrackerapp.model.Category
import retrofit2.http.GET
interface CategoryApi {
    @GET("/api/categories")
    suspend fun getCategories(): List<Category>
}