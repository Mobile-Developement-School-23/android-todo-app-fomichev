package com.example.mytodoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.usecases.GetTodoListUseCase
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TodoListRepositoryImpl,
                    private val sharedPreferencesHelper: SharedPreferencesHelper,
                    private val connection: CheckConnection): ViewModel() {



    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    val todoList = getTodoListUseCase.getTodoList()

    var modeAll: Boolean = true
    private var job: Job? = null

    private val _data = MutableSharedFlow<List<TodoItem>>()
    val data: SharedFlow<List<TodoItem>> = _data.asSharedFlow()
    init {
        if(connection.isOnline()){
            loadNetworkList()
        }
        loadData()
    }
    fun changeEnableState(todoItem: TodoItem) {
        viewModelScope.launch {
            val newItem = todoItem.copy(done = !todoItem.done)
            editTodoItemUseCase.editTodoItem(newItem)
            if (connection.isOnline()) updateNetworkItem(newItem)
        }
    }

    fun changeMode() {
        modeAll = !modeAll
        job?.cancel()
        loadData()
    }
    private fun loadNetworkList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNetworkData()
        }
    }

    fun loadData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getAllData())
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

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
    }
