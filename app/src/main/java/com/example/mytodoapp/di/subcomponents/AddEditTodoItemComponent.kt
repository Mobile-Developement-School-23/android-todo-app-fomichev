package com.example.mytodoapp.di.subcomponents

import com.example.mytodoapp.di.FragmentScope
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
import dagger.Subcomponent

/**
 * Represents a Dagger subcomponent for the add/edit todo item feature.
 * This subcomponent is responsible for providing dependencies to the AddEditTodoItemFragment.
 */
@Subcomponent()
@FragmentScope
interface AddEditTodoItemComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditTodoItemComponent
    }

    fun inject(fragment: AddEditTodoItemFragment)
}