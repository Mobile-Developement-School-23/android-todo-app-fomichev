package com.example.mytodoapp.presentation.featureTodoList.subcomponent

import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import dagger.Subcomponent

/**
 * Represents a Dagger subcomponent for the main todo list feature.
 * This subcomponent is responsible for providing dependencies to the MainTodoListFragment.
 */
@Subcomponent()
@FragmentScope
interface MainTodoListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainTodoListComponent
    }

    fun inject(fragment: MainTodoListFragment)
}