package com.example.mytodoapp.di

import android.content.Context
import androidx.room.Room
import com.example.mytodoapp.data.db.AppDataBase
import com.example.mytodoapp.data.db.TodoListDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTracksDao(database: AppDataBase): TodoListDao {
        return database.provideTodoDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDataBase{
        return Room.databaseBuilder(context,
            AppDataBase::class.java,
            "todo").build()
    }
}