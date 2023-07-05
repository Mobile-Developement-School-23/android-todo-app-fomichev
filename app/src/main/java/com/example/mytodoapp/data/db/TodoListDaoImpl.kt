package com.example.mytodoapp.data.db

import com.example.mytodoapp.data.api.TodoItemResponseMapper
import com.example.mytodoapp.domain.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoListDaoImpl@Inject constructor(private val todoListDao: TodoListDao)  {

    private val mapper = TodoListMapper()
     suspend fun addTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

     suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoListDao.deleteShopItem(todoItem.id)

    }

     suspend fun editTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    suspend fun addList(newItems: List<TodoItem>) {
        val dbModels = newItems.map { mapper.mapEntityToDbModel(it) }
        todoListDao.addList(dbModels)
    }

     suspend fun getTodoItem(todoItemId: String): TodoItem {
        val dbModel = todoListDao.getShopItem(todoItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    fun getAllData() = todoListDao.getAllFlow()
    fun getAll() = todoListDao.getAll()
}