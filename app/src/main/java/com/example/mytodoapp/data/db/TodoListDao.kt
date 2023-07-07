package com.example.mytodoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) interface for performing CRUD operations on the TodoItemDbModel table.
 */
@Dao
interface TodoListDao {

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

    @Query("SELECT * FROM todolist")
    fun getAllFlow(): Flow<List<TodoItemDbModel>>

}