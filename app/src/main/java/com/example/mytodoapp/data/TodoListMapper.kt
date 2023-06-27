package com.example.mytodoapp.data

import com.example.mytodoapp.domain.TodoItem

class TodoListMapper {

    fun mapEntityToDbModel(todoItem: TodoItem) = TodoItemDbModel(
        id = todoItem.id,
        description = todoItem.description,
        priority = todoItem.priority,
        done = todoItem.done,
        creationDate = todoItem.creationDate,
        changeDate = todoItem.changeDate,
        deadline = todoItem.deadline
    )

    fun mapDbModelToEntity(todoItemDbModel: TodoItemDbModel) = TodoItem(
        id = todoItemDbModel.id,
        description = todoItemDbModel.description,
        priority = todoItemDbModel.priority,
        done = todoItemDbModel.done,
        creationDate = todoItemDbModel.creationDate,
        changeDate = todoItemDbModel.changeDate,
        deadline = todoItemDbModel.deadline
    )

    fun mapListDbModelToListEntity(list:List<TodoItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}