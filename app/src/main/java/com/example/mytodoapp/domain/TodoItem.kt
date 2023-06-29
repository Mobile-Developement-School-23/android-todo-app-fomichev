package com.example.mytodoapp.domain

import java.net.URLDecoder
import java.util.Date


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