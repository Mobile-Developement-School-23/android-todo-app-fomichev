package com.example.mytodoapp.domain

class GetTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun getTodoItem(todoItemId: String): TodoItem {
        return todoItemRepository.getTodoItem(todoItemId)
    }
}