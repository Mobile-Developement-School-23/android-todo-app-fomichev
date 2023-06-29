package com.example.mytodoapp.data.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import java.sql.Date

@Entity(tableName = "todoList")
data class TodoItemDbModel(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "importance") var importance: Importance,
    @ColumnInfo(name = "deadline") var deadline: Long?,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "changedAt") var changedAt: Long?
) {
    fun toItem(): TodoItem = TodoItem(
        description,
        importance,
        done,
        Date(createdAt),
        changedAt?.let { Date(it) },
        deadline?.let { Date(it) },
        id
    )
    companion object {
        fun fromItem(toDoItem: TodoItem): TodoItemDbModel {
            return TodoItemDbModel(
                id = toDoItem.id,
                description = toDoItem.description,
                importance = toDoItem.priority,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                createdAt = toDoItem.creationDate.time,
                changedAt = toDoItem.changeDate?.time
            )
        }
    }
}
