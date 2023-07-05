package com.example.mytodoapp.data.api

import com.example.mytodoapp.data.network.BaseUrl
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import java.sql.Date
import javax.inject.Inject

/**
 * Maps a TodoItem object to a TodoItemResponse object.
 * @param toDoItem The TodoItem to be mapped.
 * @return The mapped TodoItemResponse.
 */

class TodoItemResponseMapper @Inject constructor(){

    fun mapToTodoItemResponse(toDoItem: TodoItem): TodoItemResponse {
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

