package com.example.mytodoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.db.TodoListDao
import com.example.mytodoapp.di.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * This module provides dependencies related to the database for dependency injection.
 * It includes methods for providing the TodoListDao and AppDataBase instances.
 * The module follows the single responsibility principle by focusing on the task
 * of providing database dependencies.
 */
@Module
object DatabaseModule {

    @ApplicationScope
    @Provides
    fun provideTracksDao(database: AppDataBase): TodoListDao {
        return database.provideTodoDao()
    }

    @ApplicationScope
    @Provides
    fun provideDataBase(context: Context): AppDataBase{
        return Room.databaseBuilder(context,
            AppDataBase::class.java,
            "todo").build()
    }
}