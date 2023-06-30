package com.example.mytodoapp.presentation

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.ServiceLocator
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.network.locale
import java.util.concurrent.TimeUnit

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val uploadWorkRequest =
            PeriodicWorkRequestBuilder<BackgroundWorkerClass>(8, TimeUnit.HOURS).setConstraints(
                constraints
            ).build()
        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(applicationContext))
        ServiceLocator.register(AppDataBase.create(locale()))
        ServiceLocator.register(TodoListRepositoryImpl(locale(), locale()))
        ServiceLocator.register(CheckConnection(applicationContext))

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "Update",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequest
        )
    }
}