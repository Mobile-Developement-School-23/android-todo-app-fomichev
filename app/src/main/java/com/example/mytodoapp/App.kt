package com.example.mytodoapp

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.TodoListRepositoryImpl
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.ServiceLocator
import com.example.mytodoapp.data.network.locale
import com.example.mytodoapp.presentation.BackgroundWorkerClass
import java.util.concurrent.TimeUnit

/**
 * This class represents the application class of the Android application.
 * It extends the Application class and is responsible for initializing the application and setting
 * up various components.
 * The class registers dependencies using ServiceLocator, sets up periodic background work using WorkManager,
 * and performs other initialization tasks such as registering SharedPreferencesHelper and database instances.
 */

class App : Application() {

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

