package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import javax.inject.Inject

/**
 * This class represents the use case for deleting a Todo Item.
 * It encapsulates the logic for deleting a Todo Item by utilizing the provided TodoItemsRepository.
 */
class DeleteTodoItemUseCase @Inject constructor(private val todoItemRepository: TodoItemsRepository) {

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoItemRepository.deleteTodoItem(todoItem)
    }
}