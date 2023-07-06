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
import com.example.mytodoapp.data.api.TodoItemResponseMapper
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.db.TodoListDao
import com.example.mytodoapp.data.db.TodoListDaoImpl
import com.example.mytodoapp.data.network.CheckConnection
import com.example.mytodoapp.data.network.ServiceLocator

import com.example.mytodoapp.di.AppComponent
import com.example.mytodoapp.di.ApplicationModule
import com.example.mytodoapp.di.DaggerAppComponent
import com.example.mytodoapp.presentation.BackgroundWorkerClass
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This class represents the application class of the Android application.
 * It extends the Application class and is responsible for initializing the application and setting
 * up various components.
 * The class registers dependencies using ServiceLocator, sets up periodic background work using WorkManager,
 * and performs other initialization tasks such as registering SharedPreferencesHelper and database instances.
 */

class App() : Application() {

    lateinit var appComponent: AppComponent
    @Inject
    lateinit var todoListDaoImpl: TodoListDaoImpl
    @Inject
    lateinit var todoItemResponseMapper: TodoItemResponseMapper
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(context = applicationContext))
            .build()
        appComponent.injectApplication(this)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val uploadWorkRequest =
            PeriodicWorkRequestBuilder<BackgroundWorkerClass>(8, TimeUnit.HOURS).setConstraints(
                constraints
            ).build()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(applicationContext))
        ServiceLocator.register(AppDataBase.create(ServiceLocator.get(Context::class)))
        ServiceLocator.register(TodoListRepositoryImpl(todoListDaoImpl, sharedPreferencesHelper,todoItemResponseMapper))
        ServiceLocator.register(CheckConnection(applicationContext))

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "Update",
            ExistingPeriodicWorkPolicy.KEEP,
            uploadWorkRequest
        )
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

