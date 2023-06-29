package com.example.mytodoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mytodoapp.data.network.TodoItemDbModel

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todoList")
    fun getShopList(): LiveData<List<TodoItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(todoItemDbModel: TodoItemDbModel)

    @Query("DELETE FROM todoList WHERE id=:todoItemId")
    suspend fun deleteShopItem(todoItemId: String)

    @Query("SELECT * FROM todoList WHERE id=:todoItemId LIMIT 1")
    suspend fun getShopItem(todoItemId: String): TodoItemDbModel

    @Query("SELECT * FROM todolist")
    fun getAll(): List<TodoItemDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(newItems: List<TodoItemDbModel>)

}