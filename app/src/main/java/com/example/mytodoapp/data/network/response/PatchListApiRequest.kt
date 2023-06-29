package com.example.mytodoapp.data.network.response

import com.google.gson.annotations.SerializedName

data class PatchListApiRequest(
    @SerializedName("list")
    val list: List<TodoItemResponse>
)
