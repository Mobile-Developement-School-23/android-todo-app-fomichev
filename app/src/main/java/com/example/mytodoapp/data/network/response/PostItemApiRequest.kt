package com.example.mytodoapp.data.network.response

import com.google.gson.annotations.SerializedName

data class PostItemApiRequest(
    @SerializedName("element")
    val item: TodoItemResponse
)
