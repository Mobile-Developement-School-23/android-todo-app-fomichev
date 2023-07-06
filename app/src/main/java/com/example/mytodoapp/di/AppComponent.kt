package com.example.mytodoapp.di

import android.content.Context
import com.example.mytodoapp.App
import com.example.mytodoapp.presentation.ui.AddEditTodoItemFragment
import com.example.mytodoapp.presentation.ui.MainActivity
import com.example.mytodoapp.presentation.ui.MainTodoListFragment
import com.example.mytodoapp.presentation.viewmodels.MainViewModel
import com.example.mytodoapp.presentation.viewmodels.TodoItemViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun injectApplication(application: App)
    fun injectMainActivity(activity: MainActivity)
    fun injectMainViewModel(fragment: MainTodoListFragment)
     fun injectTodoAddFragmentViewModel(fragment: AddEditTodoItemFragment)



}