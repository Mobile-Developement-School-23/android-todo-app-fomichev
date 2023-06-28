package com.example.mytodoapp.data

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthInterceptor : Interceptor {

    private val token = "OAuth ${ApiTokenProvider.token}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTH_HEADER, token)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val AUTH_HEADER = "Authorization"
    }


}