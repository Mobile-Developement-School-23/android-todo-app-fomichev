package com.example.mytodoapp.di.modules

import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.domain.TodoItemsRepository
import com.example.mytodoapp.domain.usecases.EditTodoItemUseCase
import dagger.Module
import dagger.Provides

@Module
class EditUseCaseModule {

    @Provides
    @FragmentScope
    fun provideEditTodoItemUseCase(repository: TodoItemsRepository): EditTodoItemUseCase {
        return EditTodoItemUseCase(repository)
    }
}