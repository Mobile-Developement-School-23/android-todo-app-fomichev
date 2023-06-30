package com.example.mytodoapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodoapp.R
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.viewmodels.MainViewModel
import com.example.mytodoapp.presentation.TodoListAdapter
import com.example.mytodoapp.presentation.viewmodels.factory
import com.example.mytodoapp.presentation.fragments.AddEditTodoItemFragment.Companion.MODE_ADD
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class MainTodoListFragment() : Fragment(){
    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var numberOfDoneTodo: TextView
    private lateinit var buttonAddTodoItem: FloatingActionButton
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
        viewModel.loadData()

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
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                updateUI(it)
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
        changeVisibility.setOnClickListener {
                viewModel.changeMode()
                when (viewModel.modeAll) {
                    true -> {
                        changeVisibility.setImageResource(R.drawable.ic_visible)
                    }
                    false -> {
                        changeVisibility.setImageResource(R.drawable.ic_invisible)
                    }
                }
        }
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
        todoListAdapter.onCheckboxItemClickListener = object :
            TodoListAdapter.OnCheckboxItemClickListener {
            override fun onCheckboxItemClick(todoItem: TodoItem) {
                viewModel.changeEnableState(todoItem)
            }
        }
    }

    private fun updateUI(list: List<TodoItem>) {
        if(viewModel.modeAll) {
            todoListAdapter.submitList(list.sortedBy { it.creationDate })
        }else{
            todoListAdapter.submitList(list.filter { !it.done }.sortedBy { it.creationDate })
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }
}