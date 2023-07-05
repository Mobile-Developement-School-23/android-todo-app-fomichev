package com.example.mytodoapp.domain

import java.sql.Date

/**
 * Data class representing a Todo Item.
 * It encapsulates the properties and behavior of a Todo Item, including its description,
 * priority, completion status, creation date, change date, deadline, and unique identifier.
 */
data class TodoItem(
    var description: String,
    var priority: Importance,
    var done: Boolean,
    var creationDate: Date,
    var changeDate: Date?,
    var deadline: Date?,
    var id: String = UNDEFINED_ID
) {

    companion object {
        val NO_DEADLINE = null
        const val UNDEFINED_ID = "0"
    }
}

enum class Importance {
    LOW,
    NORMAL,
    HIGH;
}