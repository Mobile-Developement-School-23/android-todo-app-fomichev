package com.example.mytodoapp.domain

data class TodoItem(
    var description: String,
    var priority: String = NORMAL_IMPORTANCE,
    var done: Boolean,
    var creationDate: String,
    var changeDate: String = "",
    var deadline: String = NO_DEADLINE,
    var id: Int = UNDEFINED_ID
) {

    companion object {
        const val NO_DEADLINE = "no deadline"

        const val LOW_IMPORTANCE = "low importance"
        const val NORMAL_IMPORTANCE = "normal importance"
        const val HIGH_IMPORTANCE = "high importance"

        const val UNDEFINED_ID = 0
    }

}