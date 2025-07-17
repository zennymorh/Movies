package com.zennymorh.movies.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $authToken")

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
