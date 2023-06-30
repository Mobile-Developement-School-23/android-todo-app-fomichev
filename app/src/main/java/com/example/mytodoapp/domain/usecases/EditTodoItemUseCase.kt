package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class EditTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun editTodoItem(todoItem: TodoItem) {
        todoItemRepository.editTodoItem(todoItem)
    }
}