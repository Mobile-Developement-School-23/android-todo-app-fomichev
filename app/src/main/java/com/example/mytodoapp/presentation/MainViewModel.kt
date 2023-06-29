package com.example.mytodoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope

import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.SharedPreferencesHelper
import com.example.mytodoapp.domain.EditTodoItemUseCase
import com.example.mytodoapp.domain.GetTodoListUseCase
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = TodoListRepositoryImpl(application)

    private val sharedPreferencesHelper = SharedPreferencesHelper(application)
    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)

    val todoList = getTodoListUseCase.getTodoList()

    val countComplete = todoList.value?.count { item -> item.done }

    fun changeEnableState(todoItem: TodoItem) {
        viewModelScope.launch {
            val newItem = todoItem.copy(done = !todoItem.done)
            editTodoItemUseCase.editTodoItem(newItem)
            updateNetworkItem(todoItem)
        }
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem )
        }
    }

    fun countItemsWithTrueDone(): LiveData<Int> {
        val countDoneItems = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val count = todoList.value?.count { it.done } ?: 0
            countDoneItems.postValue(count)
        }
        return countDoneItems
    }
    }
