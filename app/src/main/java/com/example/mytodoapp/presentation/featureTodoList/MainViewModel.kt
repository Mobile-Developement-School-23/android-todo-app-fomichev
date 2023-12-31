package com.example.mytodoapp.presentation.featureTodoList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private lateinit var lifecycleOwner: LifecycleOwner
    var modeAll: Boolean by mutableStateOf(true)
    private var job: Job? = null
    var showDoneItems: Boolean by mutableStateOf(true)
    private val _data = MutableStateFlow<MutableList<TodoItem>>(mutableListOf())
    val data: StateFlow<MutableList<TodoItem>> = _data
    private val _doneTodoCount = MutableStateFlow(0)
    val doneTodoCount: StateFlow<Int> = _doneTodoCount

    init {
        if (connection.isOnline()) {
            loadNetworkList()
        }
        loadData()
    }

    fun initLifecycleOwner(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    private fun loadNetworkList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNetworkData()
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllData().collect { allData ->
                val mutableList = allData.toMutableList()
                _data.value = mutableList
                updateDoneTodoCount(mutableList)
            }
        }
    }

    private fun updateDoneTodoCount(todoItems: List<TodoItem>) {
        val doneCount = todoItems.count { it.done }
        _doneTodoCount.value = doneCount
    }

    fun changeEnableState(todoItem: TodoItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newItem = todoItem.copy(done = !todoItem.done)
                editTodoItemUseCase.editTodoItem(newItem)
                if (connection.isOnline()) updateNetworkItem(newItem)
                else sharedPreferencesHelper.isNotOnline = true
                updateTodoItem(newItem)
            }
        }
    }

    fun updateTodoItem(updatedItem: TodoItem) {
        val updatedList = _data.value.toMutableList()
        val index = updatedList.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            updatedList[index] = updatedItem
            _data.value = updatedList
        }
    }

    fun changeMode() {
        showDoneItems = !showDoneItems
        modeAll = !modeAll
        job?.cancel()
        loadData()
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
