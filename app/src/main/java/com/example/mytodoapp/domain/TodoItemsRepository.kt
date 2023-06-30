package com.example.mytodoapp.domain

import androidx.lifecycle.LiveData

interface TodoItemsRepository {

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(todoItemId: String): TodoItem

    fun getTodoList(): LiveData<List<TodoItem>>

    suspend fun syncLocalListOfTodo()



}