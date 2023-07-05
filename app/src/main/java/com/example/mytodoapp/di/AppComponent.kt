package com.example.mytodoapp.di

import com.example.mytodoapp.App
import com.example.mytodoapp.presentation.ui.MainActivity
import com.example.mytodoapp.presentation.viewmodels.MainViewModel
import com.example.mytodoapp.presentation.viewmodels.TodoItemViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun injectApplication(application: App)
    fun injectMainActivity(activity: MainActivity)
    fun injectMainViewModel(): MainViewModel.MainViewModelFactory

     fun injectTodoAddFragmentViewModel(): TodoItemViewModel.TodoItemViewModelFactory


}