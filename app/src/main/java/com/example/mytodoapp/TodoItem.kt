package com.example.mytodoapp

data class TodoItem(
    var id: String,
    var description: String,
    var priority: String = NORMAL_IMPORTANCE,
    var done: Boolean,
    var creationDate: String,
    var changeDate: String = "",
    var deadline: String = NO_DEADLINE
) {

    companion object {
        const val NO_DEADLINE = "no deadline"

        const val LOW_IMPORTANCE = "low importance"
        const val NORMAL_IMPORTANCE = "normal importance"
        const val HIGH_IMPORTANCE = "high importance"
    }

}