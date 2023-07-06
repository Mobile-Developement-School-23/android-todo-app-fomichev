package com.example.mytodoapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.di.ApplicationScope
import com.example.mytodoapp.domain.usecases.AddTodoItemUseCase
import com.example.mytodoapp.domain.usecases.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.usecases.GetTodoItemUseCase
import javax.inject.Inject

/**
 * This class represents a ViewModelFactory that is responsible for creating instances of ViewModels.
 * It implements the ViewModelProvider.Factory interface.
 * The factory creates specific instances of ViewModels based on the requested modelClass.
 */

class ViewModelFactory @Inject constructor(
    private val repository: TodoListRepositoryImpl,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val connection: CheckConnection,
    private val getTodoItemUseCase: GetTodoItemUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val deleteShopItemUseCase: DeleteTodoItemUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repository, sharedPreferencesHelper, connection, editTodoItemUseCase)
            }

            TodoItemViewModel::class.java -> {
                TodoItemViewModel(
                    repository,
                    sharedPreferencesHelper,
                    connection,
                    getTodoItemUseCase,
                    addTodoItemUseCase,
                    editTodoItemUseCase,
                    deleteShopItemUseCase
                )
            }

            else -> {
                throw IllegalStateException("Unknown ViewModel")
            }
        }
        return viewModel as T
    }
}