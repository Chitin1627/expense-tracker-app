package com.example.expensetrackerapp.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class RetrofitClient(context: Context) {
    var username: String = "";
    var password: String = "";

    private val BASE_URL = "https://expense-tracker-backend-moo6.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(JwtInterceptor(context))
        .build()

    private val loginClient = OkHttpClient.Builder().build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val loginRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(loginClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    val authApi: AuthApi by lazy {
        loginRetrofit.create(AuthApi::class.java)
    }

    val expenseApi: ExpenseApi by lazy {
        retrofit.create(ExpenseApi::class.java)
    }

    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }

    val userSpendingApi: UserSpendingApi by lazy {
        retrofit.create(UserSpendingApi::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }
}
