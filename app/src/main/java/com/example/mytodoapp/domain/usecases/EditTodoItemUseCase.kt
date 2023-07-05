package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

/**
 * This class represents the use case for editing a Todo Item.
 * It encapsulates the logic for editing a Todo Item by utilizing the provided TodoItemsRepository.
 */
class EditTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun editTodoItem(todoItem: TodoItem) {
        todoItemRepository.editTodoItem(todoItem)
    }
}