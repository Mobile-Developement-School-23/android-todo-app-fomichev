package com.example.mytodoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import java.sql.Date


/**
 * Room database for TodoItems
 */
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

    /**
     * Converts the TodoItemDBModel to a TodoItem.
     */
    fun toItem(): TodoItem = TodoItem(
        description,
        importance,
        done,
        Date(createdAt),
        changedAt?.let { Date(it) },
        deadline?.let { Date(it) },
        id
    )
}
