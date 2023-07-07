package com.example.mytodoapp.domain.usecases

import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import javax.inject.Inject

/**
 * This class represents the use case for retrieving a Todo Item.
 * It encapsulates the logic for retrieving a Todo Item by utilizing the provided TodoItemsRepository.
 */
class GetTodoItemUseCase @Inject constructor(private val todoItemRepository: TodoItemsRepository) {

    suspend fun getTodoItem(todoItemId: String): TodoItem {
        return todoItemRepository.getTodoItem(todoItemId)
    }
}