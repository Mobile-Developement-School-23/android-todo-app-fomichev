package com.example.mytodoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.AppDataBase
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.NetworkAccess
import com.example.mytodoapp.data.network.SharedPreferencesHelper
import com.example.mytodoapp.domain.AddTodoItemUseCase
import com.example.mytodoapp.domain.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.EditTodoItemUseCase
import com.example.mytodoapp.domain.GetTodoItemUseCase
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


class TodoItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TodoListRepositoryImpl(application)
    private val sharedPreferencesHelper = SharedPreferencesHelper(application)
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
                uploadNetworkItem(todoItem)
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
                    updateNetworkItem(item)
                }
            }
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteShopItemUseCase.deleteTodoItem(todoItem)
            deleteNetworkItem(todoItem.id)
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