package com.example.mytodoapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.TodoItem
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This class represents the ViewModel class for the main functionality of the app.
 * It handles the business logic and data management related to the main todo list.
 */
class MainViewModel @AssistedInject constructor(private val repository: TodoListRepositoryImpl,
                                                private val sharedPreferencesHelper: SharedPreferencesHelper,
                                                private val connection: CheckConnection,
                                                private val editTodoItemUseCase: EditTodoItemUseCase): ViewModel() {
    @AssistedFactory
    interface MainViewModelFactory {
        fun create(): MainViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: MainViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }



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
            else sharedPreferencesHelper.isNotOnline = true
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
        val todoList: Flow<List<TodoItem>> = repository.getAllData()
        val countDoneItems = MutableLiveData<Int>()
        val doneItemsCount = todoList.map { list ->
            list.count { it.done }
        }
        viewModelScope.launch(Dispatchers.IO) {
            doneItemsCount.collect { count ->
                withContext(Dispatchers.Main) {
                    countDoneItems.value = count
                }
            }
        }
        return countDoneItems
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
    }
