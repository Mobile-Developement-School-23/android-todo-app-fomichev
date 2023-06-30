package com.example.mytodoapp.data.api

import com.example.mytodoapp.data.network.BaseUrl
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.google.gson.annotations.SerializedName
import java.sql.Date

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

    companion object {
        fun fromItem(toDoItem: TodoItem): TodoItemResponse {
            return TodoItemResponse(
                id = toDoItem.id,
                text = toDoItem.description,
                importance = when (toDoItem.priority) {
                    Importance.LOW -> "low"
                    Importance.NORMAL -> "basic"
                    Importance.HIGH -> "important"
                },
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                dateCreation = toDoItem.creationDate.time,
                dateChanged = when (toDoItem.changeDate) {
                    null -> toDoItem.creationDate.time
                    else -> {
                        toDoItem.changeDate!!.time
                    }
                },
                updated_by = BaseUrl.updated_by
            )
        }
    }
}
