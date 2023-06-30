package com.example.mytodoapp.presentation

import android.app.Application
import android.content.Context
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.ServiceLocator
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.network.locale

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(applicationContext))
        ServiceLocator.register(AppDataBase.create(locale()))
        ServiceLocator.register(TodoListRepositoryImpl(locale(), locale()))
        ServiceLocator.register(CheckConnection(applicationContext))
    }
}