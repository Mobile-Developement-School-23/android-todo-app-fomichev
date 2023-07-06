package com.example.mytodoapp.data.db

import android.util.Log
import com.example.mytodoapp.data.api.TodoItemResponseMapper
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoListDaoImpl@Inject constructor(private val todoListDao: TodoListDao)  {

    private val mapper = TodoListMapper()
     suspend fun addTodoItem(todoItem: TodoItem) {
         Log.d("MyLog", "addTodoItem in TodoListDaoImpl")
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

     suspend fun deleteTodoItem(todoItem: TodoItem) {
         Log.d("MyLog", "deleteTodoItem in TodoListDaoImpl")
        todoListDao.deleteShopItem(todoItem.id)

    }

     suspend fun editTodoItem(todoItem: TodoItem) {
         Log.d("MyLog", "editTodoItem in TodoListDaoImpl")
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    suspend fun addList(newItems: List<TodoItem>) {
        Log.d("MyLog", "addList in TodoListDaoImpl")
        val dbModels = newItems.map { mapper.mapEntityToDbModel(it) }
        todoListDao.addList(dbModels)
    }

     suspend fun getTodoItem(todoItemId: String): TodoItem {
         Log.d("MyLog", "getTodoItem in TodoListDaoImpl")
        val dbModel = todoListDao.getShopItem(todoItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    fun getAllData() = todoListDao.getAllFlow()

    fun getAll() = todoListDao.getAll()

}