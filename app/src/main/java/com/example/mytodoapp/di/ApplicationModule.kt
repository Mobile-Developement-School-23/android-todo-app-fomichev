package com.example.mytodoapp.di

import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context
    }

}