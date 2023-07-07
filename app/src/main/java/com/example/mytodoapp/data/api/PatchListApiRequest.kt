package com.example.mytodoapp.data.api

import com.google.gson.annotations.SerializedName

/**
 * Represents the request data for patching/updating a list via the API.
 *
 * This data class encapsulates the information necessary to update a list of
 * [TodoItemResponse] objects. It is used to serialize the request payload into JSON
 * before sending it to the API endpoint.
 *
 * @property list The list of [TodoItemResponse] objects to be updated.
 */

data class PatchListApiRequest(
    @SerializedName("list")
    val list: List<TodoItemResponse>
)
