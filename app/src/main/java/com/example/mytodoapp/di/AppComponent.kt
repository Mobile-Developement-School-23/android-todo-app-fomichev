package com.example.mytodoapp.di

import com.example.mytodoapp.App
import com.example.mytodoapp.di.modules.ApplicationModule
import com.example.mytodoapp.di.modules.DatabaseModule
import com.example.mytodoapp.di.modules.RepositoryModule
import com.example.mytodoapp.di.subcomponents.MainTodoListComponent

import com.example.mytodoapp.presentation.MainActivity
import com.example.mytodoapp.di.subcomponents.AddEditTodoItemComponent
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import dagger.Component

/**
 * This component represents the Dagger component for the application. It provides injection methods
 * for various classes
 * in the application, such as the Application class, MainActivity, MainTodoListFragment,
 * and AddEditTodoItemFragment.
 * The component is responsible for providing dependencies through its modules and follows the
 * single responsibility
 * principle by focusing on the task of dependency injection.
 */

@ApplicationScope
@Component(modules = [ApplicationModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun injectApplication(application: App)
    fun injectMainActivity(activity: MainActivity)
    fun injectMainViewModel(fragment: MainTodoListFragment)
    fun injectTodoAddFragmentViewModel(fragment: AddEditTodoItemFragment)

    fun mainTodoListComponent(): MainTodoListComponent.Factory

   fun addEditTodoItemComponent(): AddEditTodoItemComponent.Factory

    fun findViewModelFactory(): ViewModelFactory
}