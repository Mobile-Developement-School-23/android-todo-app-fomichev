package com.example.mytodoapp.presentation.featureTodoList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
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
import javax.inject.Inject

/**
 * This class represents the ViewModel for the main screen of the application. It provides data and logic
 * for managing the main todo list, including loading data, changing the display mode, changing the enable state
 * of a todo item, counting the number of completed items, and updating items in the network.
 * The ViewModel follows the single responsibility principle by focusing on the specific task of managing the main todo list.
 *
 *
 * @property repository The TodoListRepositoryImpl instance for data access and manipulation.
 * @property sharedPreferencesHelper The SharedPreferencesHelper instance for managing shared preferences.
 * @property connection The CheckConnection instance for checking network connectivity.
 * @property editTodoItemUseCase The EditTodoItemUseCase instance for editing todo items.
 */
class MainViewModel @Inject constructor(
    private val repository: TodoListRepositoryImpl,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val connection: CheckConnection,
    private val editTodoItemUseCase: EditTodoItemUseCase
) : ViewModel() {


    var modeAll: Boolean = true
    private var job: Job? = null

    private val _data = MutableSharedFlow<List<TodoItem>>()
    val data: SharedFlow<List<TodoItem>> = _data.asSharedFlow()

    init {
        if (connection.isOnline()) {
            Log.d("MyLog", "111")
            loadNetworkList()
        }
        loadData()
    }

    fun changeEnableState(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
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
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
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
                withContext(Dispatchers.Main) { countDoneItems.value = count }
            }
        }
        return countDoneItems
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
