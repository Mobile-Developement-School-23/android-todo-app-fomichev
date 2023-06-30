package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class GetTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun getTodoItem(todoItemId: String): TodoItem {
        return todoItemRepository.getTodoItem(todoItemId)
    }
}