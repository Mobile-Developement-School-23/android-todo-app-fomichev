package com.example.mytodoapp.di.subcomponents

import androidx.lifecycle.LifecycleOwner
import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import dagger.Provides
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