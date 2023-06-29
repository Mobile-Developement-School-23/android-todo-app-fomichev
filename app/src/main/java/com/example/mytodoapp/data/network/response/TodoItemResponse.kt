package com.example.mytodoapp.data.network.response

import com.example.mytodoapp.data.network.Common
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

//    fun toItem(): TodoItem = TodoItem(
//        id,
//        text,
//        when (importance) {
//            "low" -> Importance.LOW
//            "basic" -> Importance.NORMAL
//            "important" -> Importance.HIGH
//            else -> {
//                Importance.NORMAL
//            }
//        },
//        deadline?.let { Date(it) },
//        done,
//        Date(dateCreation),
//        Date(dateChanged)
//    )
//
//    companion object {
//        fun fromItem(toDoItem: TodoItem): TodoItemResponse {
//            return TodoItemResponse(
//                id = toDoItem.id,
//                text = toDoItem.description,
//                importance = when (toDoItem.importance) {
//                    Importance.LOW -> "low"
//                    Importance.NORMAL -> "normal"
//                    Importance.HIGH -> "high"
//                },
//                deadline = toDoItem.deadline.time,
//                done = toDoItem.done,
//                dateCreation = toDoItem.dateCreation.time,
//                dateChanged = when (toDoItem.dateChanged) {
//                    null -> toDoItem.dateCreation.time
//                    else -> {
//                        toDoItem.dateChanged!!.time
//                    }
//                },
//                updated_by = Common.updated_by
//            )
//        }
//    }
}
