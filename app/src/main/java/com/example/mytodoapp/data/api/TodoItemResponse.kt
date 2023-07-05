package com.example.mytodoapp.data.api

import com.example.mytodoapp.data.network.BaseUrl
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.google.gson.annotations.SerializedName
import java.sql.Date

/**
 * Represents the response data for a Todo item from the API.
 *
 * This data class contains properties that represent various attributes of a Todo item.
 * It is used to deserialize the JSON response received from the API into Kotlin objects.
 * and provides conversion function to convert between TodoItemResponse and TodoItem objects.
 */

data class TodoItemResponse(
    @SerializedName("id")
    var id: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("importance")
    var importance: String,
    @SerializedName("deadline")
    var deadline: Long?,
    @SerializedName("done")
    var done: Boolean,
    @SerializedName("created_at")
    var dateCreation: Long,
    @SerializedName("changed_at")
    var dateChanged: Long,
    @SerializedName("last_updated_by")
    var updated_by: String
) {

    fun toItem(): TodoItem = TodoItem(
        text,
        when (importance) {
            "low" -> Importance.LOW
            "basic" -> Importance.NORMAL
            "important" -> Importance.HIGH
            else -> {
                Importance.NORMAL
            }
        },
        done,
        Date(dateCreation),
        Date(dateChanged),
        deadline?.let { Date(it) },
        id
    )

}
