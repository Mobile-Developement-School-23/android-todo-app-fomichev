package com.example.mytodoapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItemsRepository
import com.example.mytodoapp.factory
import com.example.mytodoapp.presentation.AddEditTodoItemFragment.Companion.MODE_ADD
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class MainTodoListFragment() : Fragment(){
    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var todoDoneItem: String
    private lateinit var numberOfDoneTodo: TextView
    private lateinit var buttonAddTodoItem: FloatingActionButton
   // private lateinit var viewModel: MainViewModel
    private val viewModel: MainViewModel by  activityViewModels{factory()}

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
      //  viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.todoList.observe(viewLifecycleOwner) {
            todoListAdapter.submitList(it.sortedBy { it.creationDate })
        }
        buttonAddTodoItem.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, AddEditTodoItemFragment.newInstance(MODE_ADD))
                .addToBackStack(null).commit()
        }

        viewModel.todoList.observe(viewLifecycleOwner) { todoList ->
            viewModel.countItemsWithTrueDone().observe(viewLifecycleOwner) { count ->
                val todoDoneItem = getString(R.string.number_of_done_todo) + count
                numberOfDoneTodo.text = todoDoneItem
            }
        }
    }



    private fun initViews(view: View) {
        numberOfDoneTodo = view.findViewById(R.id.numberOfDoneTodo)

        buttonAddTodoItem =
            view.findViewById(R.id.button_add_todo_item)

        val changeVisibility: ShapeableImageView =
            view.findViewById(R.id.change_visibility)
        changeVisibility.tag = true
   //     changeVisibility.setOnClickListener {
   //         when (changeVisibility.tag) {
   //             false -> {
   //                 changeVisibility.setImageResource(R.drawable.ic_visible)
   //                 todoListAdapter.todoList = todoItemsRepository.getToDoList()
   //                 todoListRecyclerView.scrollToPosition(0)
   //                 changeVisibility.tag = true
   //             }

  //              true -> {
  //                  changeVisibility.setImageResource(R.drawable.ic_invisible)
  //                  todoListAdapter.todoList = todoItemsRepository.getToDoListUnDone()
   //                 changeVisibility.tag = false
    //            }
  //          }

   //     }
    }

    private fun setupRecyclerView() {

        todoListRecyclerView = requireActivity().findViewById(R.id.todoListRcView)
        todoListAdapter = TodoListAdapter()
        todoListRecyclerView.adapter = todoListAdapter
        setupClickListener()
        setupCheckboxItemClickListener()
    }

    private fun setupClickListener() {
        todoListAdapter.onTodoItemClickListener = object : TodoListAdapter.OnTodoItemClickListener {
            override fun onTodoItemClick(todoItem: TodoItem) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.root_container, AddEditTodoItemFragment.newInstance(todoItem.id))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun setupCheckboxItemClickListener() {
        todoListAdapter.onCheckboxItemClickListener = object : TodoListAdapter.OnCheckboxItemClickListener {
            override fun onCheckboxItemClick(todoItem: TodoItem) {
                viewModel.changeEnableState(todoItem)
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }
}