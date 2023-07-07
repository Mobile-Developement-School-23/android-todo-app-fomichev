package com.example.mytodoapp.domain


/**
 * This interface defines the contract for a Todo Items Repository.
 * It specifies the operations that can be performed on a collection of Todo Items.
 * The repository is responsible for adding, deleting, editing, retrieving, and syncing Todo Items.
 */

interface TodoItemsRepository {

    suspend fun addTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(todoItem: TodoItem)

    suspend fun editTodoItem(todoItem: TodoItem)

    suspend fun getTodoItem(todoItemId: String): TodoItem

    suspend fun syncListOfTodo()

}