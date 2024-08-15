package com.example.expensetrackerapp.api

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient(context: Context) {
    var username: String = "";
    var password: String = "";

    private val BASE_URL = "https://expense-tracker-backend-moo6.onrender.com/"

    // Create a logging interceptor to log the details of HTTP requests/responses
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create an OkHttpClient and add the logging interceptor
//    fun createRetrofit() {
//        val client = OkHttpClient.Builder()
//            .addInterceptor(JwtInterceptor(context))
//            .build()
//
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
//
//        val retrofitObj by lazy {
//            Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(client)  // Add the OkHttpClient to Retrofit
//                .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson for JSON conversion
//                .build()
//        }
//        retrofit = retrofitObj
//    }

    val client = OkHttpClient.Builder()
        .addInterceptor(JwtInterceptor(context))
        .build()

    private val loginClient = OkHttpClient.Builder().build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Add the OkHttpClient to Retrofit
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson for JSON conversion
            .build()
    }

    private val loginRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(loginClient)  // Add the OkHttpClient to Retrofit
            .addConverterFactory(GsonConverterFactory.create(gson))  // Use Gson for JSON conversion
            .build()
    }


    // Create instances of your API interfaces
    val authApi: AuthApi by lazy {
        loginRetrofit.create(AuthApi::class.java)
    }

    val expenseApi: ExpenseApi by lazy {
        retrofit.create(ExpenseApi::class.java)
    }

    fun setUsernamePassword(user: String, pass: String) {
        username = user
        password = pass
    }
}
