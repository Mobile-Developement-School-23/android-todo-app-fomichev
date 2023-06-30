package com.example.mytodoapp.domain

interface TodoItemsRepository {

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(todoItemId: String): TodoItem

    suspend fun syncListOfTodo()




}