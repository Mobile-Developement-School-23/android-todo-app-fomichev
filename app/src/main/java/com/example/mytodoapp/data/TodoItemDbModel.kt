package com.example.mytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mytodoapp.domain.TodoItem

//@Entity(tableName = "todo_items")
//data class TodoItemDbModel(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    val description: String,
//    val priority: String = TodoItem.NORMAL_IMPORTANCE,
//    val done: Boolean,
//    val creationDate: String,
//    val changeDate: String = "",
//    val deadline: String = TodoItem.NO_DEADLINE
//)