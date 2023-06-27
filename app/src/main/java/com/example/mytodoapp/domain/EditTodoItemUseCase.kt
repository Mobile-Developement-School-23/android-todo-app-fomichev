package com.example.mytodoapp.domain

class EditTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun editTodoItem(todoItem: TodoItem) {
        todoItemRepository.editTodoItem(todoItem)
    }
}