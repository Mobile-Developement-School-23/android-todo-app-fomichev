package com.example.mytodoapp.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.mytodoapp.domain.TodoItem

/**
 * This class is a DiffUtil.ItemCallback implementation specifically for comparing TodoItem objects.
 * It provides the necessary logic to determine whether two TodoItems represent the same item
 * and whether their contents are the same for efficient list updates.
 */
class TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
        return oldItem == newItem
    }
}