package com.example.mytodoapp.domain

class DeleteTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoItemRepository.deleteTodoItem(todoItem)
    }
}