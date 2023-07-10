package com.example.mytodoapp.di.modules

import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.di.ApplicationScope
import com.example.mytodoapp.domain.TodoItemsRepository
import dagger.Binds
import dagger.Module

/**
 * This module is responsible for binding the implementation of the TodoItemsRepository interface.
 * It binds the TodoListRepositoryImpl class as the implementation of the TodoItemsRepository interface.
 * The module follows the single responsibility principle by focusing on the task of binding
 * repository implementations.
 */
@Module
interface RepositoryModule {

    @ApplicationScope
    @Binds
    fun bindTodoRepository(todoRepository: TodoListRepositoryImpl): TodoItemsRepository

}