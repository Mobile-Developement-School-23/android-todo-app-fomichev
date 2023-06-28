package com.example.mytodoapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository

class TodoListRepositoryImpl(
    application: Application
) : TodoItemsRepository {

    private val todoListDao = AppDataBase.getInstance(application).todoListDao()
    private val mapper = TodoListMapper()


    override suspend fun addTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    override suspend fun deleteTodoItem(todoItem: TodoItem) {
        todoListDao.deleteShopItem(todoItem.id)
    }

    override suspend fun editTodoItem(todoItem: TodoItem) {
        todoListDao.addShopItem(mapper.mapEntityToDbModel(todoItem))
    }

    override suspend fun getTodoItem(todoItemId: Int): TodoItem {
        val dbModel = todoListDao.getShopItem(todoItemId)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun getTodoList(): LiveData<List<TodoItem>> = MediatorLiveData<List<TodoItem>>().apply {
        addSource(todoListDao.getShopList()) {
            value = mapper.mapListDbModelToListEntity(it)
        }
    }
}

