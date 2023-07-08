package com.example.mytodoapp

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mytodoapp.data.SharedPreferencesHelper
import com.example.mytodoapp.data.mappers.TodoItemResponseMapper
import com.example.mytodoapp.data.db.TodoLocalDbImpl
import com.example.mytodoapp.di.AppComponent
import com.example.mytodoapp.di.ApplicationModule
import com.example.mytodoapp.di.DaggerAppComponent
import com.example.mytodoapp.data.BackgroundWorkerClass
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This class represents the main Application class of the Android application.
 * It is responsible for initializing the application, setting up dependencies,
 * and registering components.
 * The class follows the single responsibility principle by focusing on the specific task
 * of managing the application lifecycle.
 */

class App() : Application() {

    lateinit var appComponent: AppComponent

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

