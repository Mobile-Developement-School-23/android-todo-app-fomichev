package com.example.mytodoapp.domain

class AddTodoItemUseCase(private val todoItemRepository: TodoItemsRepository) {

    suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemRepository.addTodoItem(todoItem)
    }
}