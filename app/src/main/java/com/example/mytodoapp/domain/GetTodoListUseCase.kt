package com.example.mytodoapp.domain

import androidx.lifecycle.LiveData

class GetTodoListUseCase(private val todoItemRepository: TodoItemsRepository) {

    fun getTodoList(): LiveData<List<TodoItem>> {
        return todoItemRepository.getTodoList()
    }
}