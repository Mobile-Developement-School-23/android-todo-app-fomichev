package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class AddTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemRepository.addTodoItem(todoItem)
    }
}