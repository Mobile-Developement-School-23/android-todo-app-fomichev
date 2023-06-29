package com.example.mytodoapp

import android.app.Application
import android.content.Context
import com.example.mytodoapp.data.AppDataBase
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.SharedPreferencesHelper

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