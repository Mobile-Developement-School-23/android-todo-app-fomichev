package com.example.mytodoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoItemDbModel::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){
    abstract val listDao: TodoListDao

    companion object{
        fun create(context: Context) = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "todo_list.db"
        ).fallbackToDestructiveMigration().build()
    }
}