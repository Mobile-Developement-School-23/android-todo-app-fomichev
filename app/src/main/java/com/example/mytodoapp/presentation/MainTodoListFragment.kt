package com.example.mytodoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.TodoItem
import com.example.mytodoapp.TodoItemsRepository
import com.example.mytodoapp.presentation.AddEditTodoItemFragment.Companion.MODE_ADD
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView


class MainTodoListFragment() : Fragment(), TodoListAdapter.OnTodoItemClickListener,
    SwipeToDeleteCallback.SwipeListener {
    private lateinit var todoListRecyclerView: RecyclerView
    private val todoItemsRepository = TodoItemsRepository()
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var todoDoneItem: String
    private lateinit var numberOfDoneTodo: TextView
    private lateinit var buttonAddTodoItem: FloatingActionButton
    private lateinit var swipeToDeleteCallback: SwipeToDeleteCallback
    private lateinit var itemTouchHelper: ItemTouchHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_todo_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()

        buttonAddTodoItem.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, AddEditTodoItemFragment.newInstance(MODE_ADD))
                .addToBackStack(null).commit()
        }

    }

    private fun initViews(view: View) {
        numberOfDoneTodo = view.findViewById(R.id.numberOfDoneTodo)
        val todoDoneItemNumber =
            todoItemsRepository.getToDoList().size - todoItemsRepository.getToDoListUnDone().size
        todoDoneItem =
            getString(R.string.number_of_done_todo) + todoDoneItemNumber.toString()
        numberOfDoneTodo.text = todoDoneItem

        buttonAddTodoItem =
            view.findViewById(R.id.button_add_todo_item)

        val changeVisibility: ShapeableImageView =
            view.findViewById(R.id.change_visibility)
        changeVisibility.tag = true
        changeVisibility.setOnClickListener {
            when (changeVisibility.tag) {
                false -> {
                    changeVisibility.setImageResource(R.drawable.ic_visible)
                    todoListAdapter.todoList = todoItemsRepository.getToDoList()
                    todoListRecyclerView.scrollToPosition(0)
                    changeVisibility.tag = true
                }

                true -> {
                    changeVisibility.setImageResource(R.drawable.ic_invisible)
                    todoListAdapter.todoList = todoItemsRepository.getToDoListUnDone()
                    changeVisibility.tag = false
                }
            }

        }
    }

    private fun setupRecyclerView() {
        todoListRecyclerView = requireActivity().findViewById(R.id.todoListRcView)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        todoListAdapter = TodoListAdapter(this)
        todoListRecyclerView.adapter = todoListAdapter
        todoListRecyclerView.layoutManager = layoutManager
        todoListAdapter.todoList = todoItemsRepository.getToDoList()
        swipeToDeleteCallback = SwipeToDeleteCallback(requireContext(), todoListAdapter)
        swipeToDeleteCallback.setSwipeListener(this)
        itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(todoListRecyclerView)

    }


    companion object {

        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }

    override fun onTodoItemClick(todoItem: TodoItem) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, AddEditTodoItemFragment.newInstance(todoItem.id.toInt()))
            .addToBackStack(null)
            .commit()
    }


    override fun onCheckboxItemClick(todoItem: TodoItem) {
        val exDoneStatus = !todoItem.done
        val newTodoItem = todoItem.copy(done = exDoneStatus)
        val position = todoListAdapter.todoList.indexOf(todoItem)
        todoItemsRepository.editToDoItem(newTodoItem)
        todoListAdapter.notifyItemChanged(position)
        updateNumberOfDoneTodo()
    }

    override fun onSwipeLeft(position: Int) {
        val item = todoItemsRepository.getToDoList()[position]
        todoItemsRepository.deleteToDoItem(item)
        updateNumberOfDoneTodo()
    }

    override fun onSwipeRight(position: Int) {
        val item = todoItemsRepository.getToDoList()[position]
        val exDoneStatus = !item.done
        val newTodoItem = item.copy(done = exDoneStatus)
        todoItemsRepository.editToDoItem(newTodoItem)
        updateNumberOfDoneTodo()
    }

    private fun updateNumberOfDoneTodo() {
        val todoDoneItemNumberUpdate =
            todoItemsRepository.getToDoList().size - todoItemsRepository.getToDoListUnDone().size
        todoDoneItem =
            getString(R.string.number_of_done_todo) + todoDoneItemNumberUpdate.toString()
        numberOfDoneTodo.text = todoDoneItem
    }

}