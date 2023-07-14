package com.example.mytodoapp.presentation.featureAddEditTodoItem


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.usecases.AddTodoItemUseCase
import com.example.mytodoapp.domain.usecases.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.usecases.GetTodoItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

/**
 * This class represents the ViewModel for managing a single todo item in the application.
 * It provides data and logic for getting, adding, editing, and deleting a todo item.
 * The ViewModel follows the single responsibility principle by focusing on the specific task of
 * managing a single todo item.
 *
 * @property repository The TodoListRepositoryImpl instance for data access and manipulation.
 * @property sharedPreferencesHelper The SharedPreferencesHelper instance for managing shared preferences.
 * @property connection The CheckConnection instance for checking network connectivity.
 * @property getTodoItemUseCase The GetTodoItemUseCase instance for getting a todo item.
 * @property addTodoItemUseCase The AddTodoItemUseCase instance for adding a todo item.
 * @property editTodoItemUseCase The EditTodoItemUseCase instance for editing a todo item.
 * @property deleteShopItemUseCase The DeleteTodoItemUseCase instance for deleting a todo item.
 */

class TodoItemViewModel @Inject constructor(
    private val repository: TodoListRepositoryImpl,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val connection: CheckConnection,
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteShopItemUseCase: DeleteTodoItemUseCase
) : ViewModel() {
    private val _todoItem = MutableStateFlow<TodoItem?>(null)
    val todoItem: StateFlow<TodoItem?> = _todoItem
    private val _errorInputName = MutableLiveData<Boolean>()
    fun getTodoItem(todoItemId: String) {
        viewModelScope.launch {
            val todoItem = getTodoItemUseCase.getTodoItem(todoItemId)
            _todoItem.emit(todoItem)
        }
    }
    suspend fun getTodoItemById(itemId: String): TodoItem {
        return getTodoItemUseCase.getTodoItem(itemId)
    }




    fun addTodoItem(inputDescription: String?, priority: Importance, done: Boolean,
        creatingDate: Date, changeDate: Date?, deadline: Date?, id: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            viewModelScope.launch(Dispatchers.IO) {
                val todoItem =
                    TodoItem(description, priority, done, creatingDate, changeDate, deadline, id)
                addTodoItemUseCase.addTodoItem(todoItem)
                if (connection.isOnline()) uploadNetworkItem(todoItem)
                else sharedPreferencesHelper.isNotOnline = true
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteShopItemUseCase.deleteTodoItem(todoItem)
            if (connection.isOnline()) deleteNetworkItem(todoItem.id)
            else sharedPreferencesHelper.isNotOnline = true
        }
    }

    fun editTodoItem(inputDescription: String?, priority: Importance, done: Boolean,
        creatingDate: Date, changeDate: Date?, deadline: Date?, id: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            _todoItem.value?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    val item = it.copy(
                        description = description,
                        priority = priority,
                        done = done,
                        changeDate = changeDate,
                        deadline = deadline,
                        id = id)
                    editTodoItemUseCase.editTodoItem(item)
                    if (connection.isOnline()) updateNetworkItem(item)
                    else sharedPreferencesHelper.isNotOnline = true }
            }
        }
    }

    private fun uploadNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.postNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
            when (response) {
                is NetworkAccess.Success -> sharedPreferencesHelper
                    .putRevision(response.data.revision)

                is NetworkAccess.Error -> sharedPreferencesHelper.networkAccessError = true
            }
        }
    }


    private fun deleteNetworkItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.deleteNetworkItem(sharedPreferencesHelper.getLastRevision(), id)
            when (response) {
                is NetworkAccess.Success -> sharedPreferencesHelper
                    .putRevision(response.data.revision)

                is NetworkAccess.Error -> sharedPreferencesHelper.networkAccessError = true
            }
        }
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
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