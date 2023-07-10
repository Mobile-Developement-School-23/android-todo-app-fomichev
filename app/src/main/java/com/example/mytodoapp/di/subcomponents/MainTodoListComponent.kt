package com.example.mytodoapp.di.subcomponents

import com.example.mytodoapp.di.modules.EditUseCaseModule
import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import com.example.mytodoapp.presentation.featureTodoList.MainViewModel
import dagger.Subcomponent

/**
 * Represents a Dagger subcomponent for the main todo list feature.
 * This subcomponent is responsible for providing dependencies to the MainTodoListFragment.
 */

@FragmentScope
@Subcomponent(modules = [EditUseCaseModule::class])
interface MainTodoListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainTodoListComponent
    }

    fun inject(fragment: MainTodoListFragment)

    fun viewModel(): MainViewModel

}