package com.example.mytodoapp.data.api

import com.google.gson.annotations.SerializedName

/**
 * Represents the request data for creating a new item via the API.
 *
 * This data class encapsulates the information necessary to create a new item
 * using a [TodoItemResponse] object. It is used to serialize the request payload into JSON
 * before sending it to the API endpoint.
 *
 * @property item The [TodoItemResponse] object representing the new item to be created.
 */

data class PostItemApiRequest(
    @SerializedName("element")
    val item: TodoItemResponse
)
