package com.example.mytodoapp.di

import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.domain.TodoItemsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindTodoRepository(todoRepository: TodoListRepositoryImpl): TodoItemsRepository

}