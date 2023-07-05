package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import javax.inject.Inject

/**
 * This class represents the use case for adding a new Todo Item.
 * It encapsulates the logic for adding a Todo Item by utilizing the provided TodoItemsRepository.
 */
class AddTodoItemUseCase @Inject constructor(private val todoItemRepository: TodoItemsRepository) {

    suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemRepository.addTodoItem(todoItem)
    }
}