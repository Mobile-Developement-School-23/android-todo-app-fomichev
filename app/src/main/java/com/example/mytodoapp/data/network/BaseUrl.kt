package com.example.mytodoapp.data.network

/**
 * The BaseUrl object represents the base URL configuration for the TodoBackend API.
 * It provides a single responsibility of defining the base URL and creating a Retrofit API client for making HTTP requests.
 */
object BaseUrl {

    private const val baseURL:String = "https://beta.mrdekk.ru/todobackend/"
    var updated_by = "1"
    val retrofitApi:RetrofitApi get() = RetrofitClient.getClient(baseURL).create(RetrofitApi::class.java)
}