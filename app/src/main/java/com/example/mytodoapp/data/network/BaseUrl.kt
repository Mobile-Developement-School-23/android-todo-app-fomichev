package com.example.mytodoapp.data.network

object BaseUrl {

    private const val baseURL:String = "https://beta.mrdekk.ru/todobackend/"
    var updated_by = "1"
    val retrofitService:RetrofitService get() = RetrofitClient.getClient(baseURL).create(RetrofitService::class.java)


}