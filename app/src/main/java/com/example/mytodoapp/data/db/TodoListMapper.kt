package com.example.mytodoapp.data.db

import com.example.mytodoapp.domain.TodoItem

class TodoListMapper {

    fun mapEntityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        description = todoItem.description,
        importance = todoItem.priority,
        done = todoItem.done,
        createdAt = todoItem.creationDate!!.time,
        changedAt = todoItem.changeDate?.time,
        deadline = todoItem.deadline?.time
    )

    fun mapDbModelToEntity(todoItemDbModel: TodoItemDbModel) = TodoItem(
        id = todoItemDbModel.id,
        description = todoItemDbModel.description,
        priority = todoItemDbModel.importance,
        done = todoItemDbModel.done,
        creationDate = java.sql.Date(todoItemDbModel.createdAt),
        changeDate = todoItemDbModel.changedAt?.let { java.sql.Date(it) },
        deadline = todoItemDbModel.deadline?.let { java.sql.Date(it) }
    )

    fun mapListDbModelToListEntity(list:List<TodoItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}