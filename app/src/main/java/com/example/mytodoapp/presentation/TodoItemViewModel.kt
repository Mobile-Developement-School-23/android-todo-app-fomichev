package com.example.mytodoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.domain.AddTodoItemUseCase
import com.example.mytodoapp.domain.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.EditTodoItemUseCase
import com.example.mytodoapp.domain.GetTodoItemUseCase
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItem.Companion.NO_DEADLINE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TodoListRepositoryImpl(application)

    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteTodoItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun getTodoItem(todoItemId: Int) {
        viewModelScope.launch {
            val item = getTodoItemUseCase.getTodoItem(todoItemId)
            _todoItem.value = item
        }
    }

    fun addTodoItem(inputDescription: String?, priority: String, done: Boolean, creatingDate: String, changeDate:String, deadline: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            viewModelScope.launch {
                val todoItem = TodoItem(description, priority, done, creatingDate, changeDate, deadline)
                addTodoItemUseCase.addTodoItem(todoItem)
            }

        }
    }



    fun editTodoItem(inputDescription: String?, priority: String, done: Boolean, creatingDate: String, changeDate:String, deadline: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            _todoItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(description = description, priority=priority, done=done, changeDate=changeDate, deadline=deadline)
                    editTodoItemUseCase.editTodoItem(item)

                }
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteTodoItem(todoItem)
        }
    }

    private fun parseName(inputDescription: String?): String {
        return inputDescription?.trim() ?: ""
    }

    private fun validateInput(description: String): Boolean {
        var result = true
        if (description.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        return result
    }
}