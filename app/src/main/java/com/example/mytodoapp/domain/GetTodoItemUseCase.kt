package com.example.mytodoapp.domain

class GetTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun getTodoItem(todoItemId: Int): TodoItem {
        return todoItemRepository.getTodoItem(todoItemId)
    }
}