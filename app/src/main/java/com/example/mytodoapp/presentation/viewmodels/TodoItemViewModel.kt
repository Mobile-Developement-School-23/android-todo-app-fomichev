package com.example.mytodoapp.presentation.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.domain.usecases.AddTodoItemUseCase
import com.example.mytodoapp.domain.usecases.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.usecases.GetTodoItemUseCase
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException
import java.sql.Date


class TodoItemViewModel(private val repository: TodoListRepositoryImpl,
                        private val sharedPreferencesHelper: SharedPreferencesHelper,
                        private val connection: CheckConnection
) : ViewModel() {



    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteTodoItemUseCase(repository)
    private val _errorInputName = MutableLiveData<Boolean>()


    private val _todoItem = MutableLiveData<TodoItem>()
    val todoItem: LiveData<TodoItem>
        get() = _todoItem

    fun getTodoItem(todoItemId: String) {
        viewModelScope.launch {
            val item = getTodoItemUseCase.getTodoItem(todoItemId)
            _todoItem.value = item
        }
    }

    fun addTodoItem(inputDescription: String?, priority: Importance, done: Boolean, creatingDate: Date, changeDate:Date?, deadline: Date?, id: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            viewModelScope.launch {
                val todoItem = TodoItem(description, priority, done, creatingDate, changeDate, deadline, id)
                addTodoItemUseCase.addTodoItem(todoItem)
                if (connection.isOnline()) uploadNetworkItem(todoItem)
                else sharedPreferencesHelper.isNotOnline = true
            }
        }
    }



    fun editTodoItem(inputDescription: String?, priority: Importance, done: Boolean, creatingDate: Date, changeDate:Date?, deadline: Date?, id: String) {
        val description = parseName(inputDescription)
        val fieldsValid = validateInput(description)
        if (fieldsValid) {
            _todoItem.value?.let {
                viewModelScope.launch {
                    val item = it.copy(description = description, priority=priority, done=done, changeDate=changeDate, deadline=deadline, id = id)
                    editTodoItemUseCase.editTodoItem(item)
                    if (connection.isOnline()) updateNetworkItem(item)
                    else sharedPreferencesHelper.isNotOnline = true
                }
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteTodoItem(todoItem)
            if (connection.isOnline()) deleteNetworkItem(todoItem.id)
            else sharedPreferencesHelper.isNotOnline = true
        }
    }

    private fun uploadNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.postNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error -> {
                    sharedPreferencesHelper.networkAccessError = true
                }
            }
        }
    }


    private fun deleteNetworkItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.deleteNetworkItem(sharedPreferencesHelper.getLastRevision(), id)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error -> {
                    sharedPreferencesHelper.networkAccessError = true
                }
            }
        }
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem )
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