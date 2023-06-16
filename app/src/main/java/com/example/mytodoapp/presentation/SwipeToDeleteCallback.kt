package com.example.mytodoapp.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.TodoItemsRepository


class SwipeToDeleteCallback(context: Context, private val adapter: TodoListAdapter) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
    private val todoItemsRepository = TodoItemsRepository()
    private var swipeListener: SwipeListener? = null

    interface SwipeListener {
        fun onSwipeLeft(position: Int)
        fun onSwipeRight(position: Int)
    }

    fun setSwipeListener(listener: SwipeListener) {
        swipeListener = listener
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                val position = viewHolder.adapterPosition
                val todoItem = adapter.todoList[position]
                swipeListener?.onSwipeLeft(position)
                todoItemsRepository.deleteToDoItem(todoItem)
                adapter.notifyDataSetChanged()
            }

            ItemTouchHelper.RIGHT -> {
                val position = viewHolder.adapterPosition
                val todoItem = adapter.todoList[position]
                val exDoneStatus = !todoItem.done
                val newTodoItem = todoItem.copy(done = exDoneStatus)
                swipeListener?.onSwipeRight(position)
                todoItemsRepository.editToDoItem(newTodoItem)
                adapter.notifyItemChanged(position)
            }
        }

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val maxSwipeDistance = itemView.width / 2
            val restrictedDX = dX.coerceIn(-maxSwipeDistance.toFloat(), maxSwipeDistance.toFloat())
            itemView.translationX = restrictedDX
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}