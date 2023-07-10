package com.example.mytodoapp.presentation.featureTodoList

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.FragmentMainTodoListBinding
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditFragmentViewController.Companion.MODE_ADD
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainTodoListFragmentViewController (
    private val fragment: MainTodoListFragment,
    private val binding: FragmentMainTodoListBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
) {

    private lateinit var todoListAdapter:TodoListAdapter

    private val fragManager = fragment.requireActivity().supportFragmentManager

    fun initViews() = with(binding) {
        setupRecyclerView()

        binding.buttonAddTodoItem.setOnClickListener {
            openAddEditTodoItemFragment()
        }
        lifecycleOwner.lifecycleScope.launch {
            viewModel.countItemsWithTrueDone().collect {count ->
                val todoDoneItem = fragment.requireActivity().getString(R.string.number_of_done_todo) + count
                binding.numberOfDoneTodo.text = todoDoneItem

            }
        }
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    updateUI(it)
                }
            }
        }

        changeVisibility.setOnClickListener {
            viewModel.changeMode()
            updateChangeVisibilityIcon()
            updateUI(viewModel.data.value)
        }
    }
    private fun updateChangeVisibilityIcon() {
        val visibilityIconRes = if (viewModel.modeAll) R.drawable.ic_visible
        else R.drawable.ic_invisible

        binding.changeVisibility.setImageResource(visibilityIconRes)
    }

    private fun setupRecyclerView() = with(binding) {
        todoListAdapter = TodoListAdapter()
        todoListRecyclerView.adapter = todoListAdapter
        setupClickListener()
        setupCheckboxItemClickListener()
    }

    private fun setupClickListener() {
        todoListAdapter.onTodoItemClickListener = object : TodoListAdapter.OnTodoItemClickListener {
            override fun onTodoItemClick(todoItem: TodoItem) {
                fragManager.beginTransaction()
                    .replace(R.id.rootContainer, AddEditTodoItemFragment.newInstance(todoItem.id))
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
        if (viewModel.modeAll) {
            todoListAdapter.submitList(list.sortedBy { it.creationDate })
        } else {
            todoListAdapter.submitList(list.filter { !it.done }.sortedBy { it.creationDate })
        }
    }

    private fun openAddEditTodoItemFragment(){
        fragManager.beginTransaction()
            .replace(R.id.rootContainer, AddEditTodoItemFragment.newInstance(MODE_ADD))
            .addToBackStack(null).commit()
    }
}