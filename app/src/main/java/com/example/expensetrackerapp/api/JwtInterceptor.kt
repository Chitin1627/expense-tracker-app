package com.example.expensetrackerapp.api

import android.content.Context
import com.example.expensetrackerapp.data.getToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class JwtInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = getToken(context)
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
