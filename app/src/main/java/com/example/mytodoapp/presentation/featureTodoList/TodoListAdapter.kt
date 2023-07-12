package com.example.mytodoapp.presentation.featureTodoList

//import android.graphics.Paint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mytodoapp.R
//import com.example.mytodoapp.domain.Importance
//import com.example.mytodoapp.domain.TodoItem
//import com.example.mytodoapp.presentation.utils.TodoItemDiffCallback
//
///**
// * This class represents the adapter for a RecyclerView used to display a list of TodoItems.
// * It provides the necessary functionality to bind TodoItem data to the views in the list.
// */
//
//class TodoListAdapter :
//    ListAdapter<TodoItem, TodoListAdapter.TodoListViewHolder>(TodoItemDiffCallback()) {
//
//    var onTodoItemClickListener: OnTodoItemClickListener? = null
//    var onCheckboxItemClickListener: OnCheckboxItemClickListener? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(
//            R.layout.todo_item,
//            parent,
//            false
//        )
//        return TodoListViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
//        val todoItem = getItem(position)
//        with(holder) {
//            bindTodoItem(todoItem)
//            bindPriority(todoItem)
//            bindDeadline(todoItem)
//            setupClickListeners(todoItem)
//        }
//    }
//
//    private fun TodoListViewHolder.bindTodoItem(todoItem: TodoItem) {
//        todoDescription.text = todoItem.description
//        todoDone.isChecked = todoItem.done
//        if (todoDone.isChecked) {
//            todoDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//        } else {
//            todoDescription.paintFlags = 0
//        }
//    }
//
//    private fun TodoListViewHolder.bindPriority(todoItem: TodoItem) {
//        when (todoItem.priority) {
//            Importance.LOW -> {
//                buttonPriority.setBackgroundResource(R.drawable.ic_low_priority)
//                buttonPriority.visibility = View.VISIBLE
//                checkboxDone.setButtonDrawable(R.drawable.checkbox_selection)
//            }
//            Importance.HIGH -> {
//                buttonPriority.setBackgroundResource(R.drawable.ic_high_priority)
//                buttonPriority.visibility = View.VISIBLE
//                todoDone.setButtonDrawable(R.drawable.checkbox_selection_high)
//                checkboxDone.setButtonDrawable(R.drawable.checkbox_selection_high)
//            }
//            else -> {
//                buttonPriority.visibility = View.GONE
//                checkboxDone.setButtonDrawable(R.drawable.checkbox_selection)
//            }
//        }
//    }
//
//    private fun TodoListViewHolder.bindDeadline(todoItem: TodoItem) {
//        if (todoItem.deadline != null) {
//            tvDeadline.visibility = View.VISIBLE
//            tvDeadline.text = todoItem.deadline.toString()
//        } else {
//            tvDeadline.visibility = View.GONE
//        }
//    }
//
//    private fun TodoListViewHolder.setupClickListeners(todoItem: TodoItem) {
//        itemView.setOnClickListener {
//            onTodoItemClickListener?.onTodoItemClick(todoItem)
//        }
//        todoItemInfoButton.setOnClickListener {
//            onTodoItemClickListener?.onTodoItemClick(todoItem)
//        }
//        todoDone.setOnClickListener {
//            onCheckboxItemClickListener?.onCheckboxItemClick(todoItem)
//        }
//    }
//
//    class TodoListViewHolder(todoItemView: View) : RecyclerView.ViewHolder(todoItemView) {
//        val todoDone: CheckBox = todoItemView.findViewById(R.id.checkboxDone)
//        val todoDescription: TextView = todoItemView.findViewById(R.id.todoDescription)
//        val buttonPriority: ImageView = todoItemView.findViewById(R.id.buttonPriority)
//        val tvDeadline: TextView = todoItemView.findViewById(R.id.tvDeadline)
//        val todoItemInfoButton: ImageButton = todoItemView.findViewById(R.id.todoItemInfoButton)
//        val checkboxDone: CheckBox = todoItemView.findViewById(R.id.checkboxDone)
//    }
//
//
//
//    interface OnTodoItemClickListener {
//        fun onTodoItemClick(todoItem: TodoItem)
//    }
//
//    interface OnCheckboxItemClickListener {
//        fun onCheckboxItemClick(todoItem: TodoItem)
//    }
//}