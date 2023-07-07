package com.example.mytodoapp.data.api

import com.google.gson.annotations.SerializedName

/**
 * Represents the response data for the "getList" API endpoint.
 *
 * This data class contains the response status, a list of [TodoItemResponse] objects,
 * and the revision number. It is used to deserialize the JSON response received from
 * the API into Kotlin objects.
 *
 * @property status The status of the API response.
 * @property list The list of [TodoItemResponse] objects representing the retrieved data.
 * @property revision The revision number indicating the version of the data.
 */
data class GetListApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemResponse>,

    @SerializedName("revision")
    val revision: Int

)
