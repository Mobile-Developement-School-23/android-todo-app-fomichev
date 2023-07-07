package com.example.mytodoapp.data.network

import retrofit2.Response

/**
 * The NetworkAccess class represents the result of a network operation.
 * It is a sealed class with two subclasses: Success and Error.
 * Success represents a successful network operation with associated data.
 * Error represents a failed network operation with an error response.
 */

sealed class NetworkAccess<out T> {
    data class Success<out T>(val data: T): NetworkAccess<T>()
    data class Error<T>(val response: Response<T>): NetworkAccess<T>()
}

fun <T> Response<T>.parseResponse(): NetworkAccess<T> {
    return if (this.isSuccessful && this.body() != null) {
        val responseBody = this.body()
        NetworkAccess.Success(responseBody!!)
    } else NetworkAccess.Error(this)
}
