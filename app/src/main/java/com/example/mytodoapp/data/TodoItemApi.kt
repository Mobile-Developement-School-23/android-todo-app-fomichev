package com.example.mytodoapp.data


data class TodoItemApi(
   var id: String = "",
   var text: String = "",
   var importance: String = "",
   var deadline: Long = 0L,
   var done: Boolean = false,
   var color: String = "",
    var createdAt: Long = 0L,
   var changedAt: Long = 0L,
   var lastUpdateBy: String = ""
)



data class TodoListResponse(
  val status: String,
  val list: List<TodoItemApi>,
  val revision: Int
)


data class TodoResponse(
   val status: String,
   val todoItem: TodoItemApi,
   val revision: Int
)



data class TodoBody(
   val todoItem: TodoItemApi
)