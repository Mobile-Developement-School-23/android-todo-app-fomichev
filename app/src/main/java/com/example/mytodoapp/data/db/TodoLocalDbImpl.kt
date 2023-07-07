package com.example.mytodoapp.data.db

import com.example.mytodoapp.data.mappers.TodoListMapper
import com.example.mytodoapp.domain.TodoItem
import javax.inject.Inject

/**
 * This class provides methods to perform CRUD operations for a todo list.
 * It interacts with a [TodoListDao] instance to access the underlying data source.
 */
class TodoLocalDbImpl @Inject constructor(
    private val todoListDao: TodoListDao,
    private val mapper: TodoListMapper
) {


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