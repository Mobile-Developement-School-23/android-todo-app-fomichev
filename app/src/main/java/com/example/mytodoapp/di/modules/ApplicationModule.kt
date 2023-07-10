package com.example.mytodoapp.di.modules

import android.content.Context
import com.example.mytodoapp.di.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * This module provides the application context for dependency injection.
 * It is responsible for providing the application context to other components and follows
 * the single responsibility principle by focusing on the task of providing the context.
 *
 * @param context The application context.
 */
@Module
class ApplicationModule(private val context: Context) {

    @ApplicationScope
    @Provides
    fun provideContext(): Context {
        return context
    }

}