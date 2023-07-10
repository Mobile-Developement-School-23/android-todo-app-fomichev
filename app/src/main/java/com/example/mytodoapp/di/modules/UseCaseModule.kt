package com.example.mytodoapp.di.modules

import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.domain.TodoItemsRepository
import com.example.mytodoapp.domain.usecases.AddTodoItemUseCase
import com.example.mytodoapp.domain.usecases.DeleteTodoItemUseCase
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import com.example.mytodoapp.domain.usecases.GetTodoItemUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    @FragmentScope
    fun provideAddTodoItemUseCase(repository: TodoItemsRepository): AddTodoItemUseCase {
        return AddTodoItemUseCase(repository)
    }

    @Provides
    @FragmentScope
    fun provideGetTodoItemUseCase(repository: TodoItemsRepository): GetTodoItemUseCase {
        return GetTodoItemUseCase(repository)
    }

    @Provides
    @FragmentScope
    fun provideEditTodoItemUseCase(repository: TodoItemsRepository): EditTodoItemUseCase {
        return EditTodoItemUseCase(repository)
    }

    @Provides
    @FragmentScope
    fun provideDeleteTodoItemUseCase(repository: TodoItemsRepository): DeleteTodoItemUseCase {
        return DeleteTodoItemUseCase(repository)
    }

}