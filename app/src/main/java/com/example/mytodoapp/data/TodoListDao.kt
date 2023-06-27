package com.example.mytodoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_items")
    fun getShopList(): LiveData<List<TodoItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(todoItemDbModel: TodoItemDbModel)

    @Query("DELETE FROM todo_items WHERE id=:todoItemId")
    suspend fun deleteShopItem(todoItemId: Int)

    @Query("SELECT * FROM todo_items WHERE id=:todoItemId LIMIT 1")
    suspend fun getShopItem(todoItemId: Int): TodoItemDbModel
}