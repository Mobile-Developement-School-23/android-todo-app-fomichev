package com.example.mytodoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * The AppDataBase class represents the Room database for the TodoList application.
 * It provides access to the data access objects (DAOs) and manages the underlying SQLite database.
 */
@Database(entities = [TodoItemDbModel::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){
    abstract fun provideTodoDao(): TodoListDao

    companion object{
        fun create(context: Context) = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "todo_list.db"
        ).fallbackToDestructiveMigration().build()
    }
}