package com.example.mytodoapp

import com.example.mytodoapp.TodoItem.Companion.HIGH_IMPORTANCE
import com.example.mytodoapp.TodoItem.Companion.LOW_IMPORTANCE
import com.example.mytodoapp.TodoItem.Companion.NORMAL_IMPORTANCE


class TodoItemsRepository {

    fun getToDoList(): List<TodoItem> {
        return todoItemList
    }

    fun getToDoItemById(itemId: Int): TodoItem? {
        return todoItemList.find {
            it.id.toInt() == itemId
        }
    }

    fun getToDoListUnDone(): List<TodoItem> {
        return todoItemList.filter { !it.done }.toList()
    }

    fun addToDoItem(todoItem: TodoItem) {
        todoItemList.add(todoItem)
        idGenerate++

    }

    fun editToDoItem(todoItem: TodoItem) {
        val position = todoItemList.indexOf(getToDoItemById(todoItem.id.toInt()))
        todoItemList[position] = todoItem
    }

    fun deleteToDoItem(todoItem: TodoItem) {
        todoItemList.remove(todoItem)

    }

    companion object {
        val todoItemList = mutableListOf<TodoItem>(
            TodoItem(
                "0",
                "Первое дело",
                LOW_IMPORTANCE,
                false,
                "14-января-2023",
                "14-января-2023"
            ),
            TodoItem(
                "1",
                "Первое дело если судить по id",
                LOW_IMPORTANCE,
                true,
                "14-мая-2023",
                "14-мая-2023"
            ),
            TodoItem(
                "2",
                "Второе дело",
                NORMAL_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023",
                "28-июня-2023"
            ),
            TodoItem(
                "3",
                "Важное дело",
                HIGH_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem("4", "Дело еще важней предыдущего", HIGH_IMPORTANCE,
                true, "sdfs", "sdfsd"),
            TodoItem("5", "Самое важное дело просто жесть", HIGH_IMPORTANCE,
                true, "sdfs", "sdfsd"),
            TodoItem(
                "6",
                "Очень длинное дело просто чтобы проверить три точки в конце. Очень " +
                        "длинное дело просто чтобы проверить три точки в конце. Очень длинное" +
                        " дело просто чтобы проверить три точки в конце. Очень длинное дело " +
                        "просто чтобы проверить три точки в конце",
                NORMAL_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "7",
                "Тут просто текст",
                LOW_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023",
                "15-августа-2023"
            ),
            TodoItem(
                "8",
                "Еще Тут просто текст",
                HIGH_IMPORTANCE,
                true,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "9",
                "Нужно больше текста Богу текста",
                HIGH_IMPORTANCE,
                true,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "10",
                "Продолжаем писать текст задач",
                NORMAL_IMPORTANCE,
                true,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "11",
                "Помыть посуду (неожиданно:) )",
                LOW_IMPORTANCE,
                true,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "12",
                "Опять пошел текст, можно даже длинный текст, чтобы " +
                        "снова посмотреть на три точки. Обожаю их, так бы всю жизнь и смотрел.",
                HIGH_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "13",
                "Заканчиваем писать дела",
                LOW_IMPORTANCE,
                false,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem(
                "14",
                "Осталось еще одно",
                HIGH_IMPORTANCE,
                true,
                "11-июня-2023",
                "11-июня-2023"
            ),
            TodoItem("15", "И вот оно", HIGH_IMPORTANCE, true,
                "11-июня-2023", "11-июня-2023")
        )
        var idGenerate = todoItemList.size
    }
}