package com.example.mytodoapp.data.api

import com.google.gson.annotations.SerializedName

/**
 * Represents the response data for the "postItem" API endpoint.
 *
 * This data class contains the response status, the created [TodoItemResponse] object,
 * and the revision number. It is used to deserialize the JSON response received from
 * the API into Kotlin objects.
 *
 * @property status The status of the API response.
 * @property item The [TodoItemResponse] object representing the item that was created.
 * @property revision The revision number indicating the version of the data.
 */

data class PostItemApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val item: TodoItemResponse,

    @SerializedName("revision")
    val revision: Int

)
