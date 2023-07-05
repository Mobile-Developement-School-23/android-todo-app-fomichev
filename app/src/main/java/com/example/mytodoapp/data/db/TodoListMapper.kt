package com.example.mytodoapp.data.db

import com.example.mytodoapp.domain.TodoItem


class TodoListMapper {

    /**
     * Mapper from domain entity to local Database
     */

    fun mapEntityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        description = todoItem.description,
        importance = todoItem.priority,
        done = todoItem.done,
        createdAt = todoItem.creationDate!!.time,
        changedAt = todoItem.changeDate?.time,
        deadline = todoItem.deadline?.time
    )

    /**
     * Mapper from local Database to domain entity
     */

    fun mapDbModelToEntity(todoItemDbModel: TodoItemDbModel) = TodoItem(
        id = todoItemDbModel.id,
        description = todoItemDbModel.description,
        priority = todoItemDbModel.importance,
        done = todoItemDbModel.done,
        creationDate = java.sql.Date(todoItemDbModel.createdAt),
        changeDate = todoItemDbModel.changedAt?.let { java.sql.Date(it) },
        deadline = todoItemDbModel.deadline?.let { java.sql.Date(it) }
    )

}