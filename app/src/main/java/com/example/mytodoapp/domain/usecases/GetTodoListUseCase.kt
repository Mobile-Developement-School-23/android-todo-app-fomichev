package com.example.mytodoapp.domain.usecases

import androidx.lifecycle.LiveData
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class GetTodoListUseCase(private val todoItemRepository: TodoItemsRepository) {

    fun getTodoList(): LiveData<List<TodoItem>> {
        return todoItemRepository.getTodoList()
    }
}