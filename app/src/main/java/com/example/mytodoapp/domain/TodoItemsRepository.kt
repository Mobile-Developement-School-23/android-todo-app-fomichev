package com.example.mytodoapp.domain

import androidx.lifecycle.LiveData
import com.example.mytodoapp.domain.TodoItem.Companion.HIGH_IMPORTANCE
import com.example.mytodoapp.domain.TodoItem.Companion.LOW_IMPORTANCE
import com.example.mytodoapp.domain.TodoItem.Companion.NORMAL_IMPORTANCE


interface TodoItemsRepository {

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(todoItemId: Int): TodoItem

    fun getTodoList(): LiveData<List<TodoItem>>

}