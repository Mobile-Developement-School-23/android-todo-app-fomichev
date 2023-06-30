package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class DeleteTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoItemRepository.deleteTodoItem(todoItem)
    }
}