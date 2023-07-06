package com.example.mytodoapp.di

import com.example.mytodoapp.App
import com.example.mytodoapp.presentation.ui.AddEditTodoItemFragment
import com.example.mytodoapp.presentation.ui.MainActivity
import com.example.mytodoapp.presentation.ui.MainTodoListFragment
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

}