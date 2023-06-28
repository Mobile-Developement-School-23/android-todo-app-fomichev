package com.example.mytodoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.domain.EditTodoItemUseCase
import com.example.mytodoapp.domain.GetTodoListUseCase
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = TodoListRepositoryImpl(application)

    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)

    val todoList = getTodoListUseCase.getTodoList()



    fun changeEnableState(todoItem: TodoItem) {
        viewModelScope.launch {
            val newItem = todoItem.copy(done = !todoItem.done)
            editTodoItemUseCase.editTodoItem(newItem)
        }
    }

    fun countItemsWithTrueDone():Int {
        return todoList.value?.count { it.done } ?: 0
    }

}