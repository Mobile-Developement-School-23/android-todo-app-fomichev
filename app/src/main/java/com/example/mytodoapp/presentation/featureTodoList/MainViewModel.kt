package com.example.mytodoapp.presentation.featureTodoList

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

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

    private val _data = MutableStateFlow<List<TodoItem>>(emptyList())
    val data: StateFlow<List<TodoItem>> = _data.asStateFlow()
    private lateinit var lifecycleOwner: LifecycleOwner

    init {
        if (connection.isOnline()) {
            loadNetworkList()
        }
        loadData()
    }
    fun initLifecycleOwner(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    fun changeEnableState(todoItem: TodoItem) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val newItem = todoItem.copy(done = !todoItem.done)
                editTodoItemUseCase.editTodoItem(newItem)
                if (connection.isOnline()) updateNetworkItem(newItem)
                else sharedPreferencesHelper.isNotOnline = true
            }
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

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getAllData())
        }
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
        }
    }

    fun countItemsWithTrueDone(): Flow<Int> {
        val todoList: Flow<List<TodoItem>> = repository.getAllData()
        return todoList.map { list ->
            list.count { it.done }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
