package com.example.mytodoapp.presentation.featureTodoList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.databinding.FragmentMainTodoListBinding
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment.Companion.MODE_ADD
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class represents a fragment that displays the main todo list in an Android application.
 * It handles the user interface elements and logic for viewing, editing, and managing todo items.
 * The class follows the single responsibility principle by focusing on the specific task of managing
 * the main todo list.
 */

class MainTodoListFragment() : Fragment() {

    private lateinit var binding: FragmentMainTodoListBinding
    private lateinit var todoListAdapter: TodoListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: MainViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent
            .mainTodoListComponent()
            .create()
            .inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        setupRecyclerView()
        initViews()
    }

    private fun initViews() = with(binding) {
        changeVisibility.tag = true
        changeVisibility.setOnClickListener {
            viewModel.changeMode()
            updateChangeVisibilityIcon()
        }
        binding.buttonAddTodoItem.setOnClickListener {
            openAddEditTodoItemFragment()
        }
        viewModel.countItemsWithTrueDone().observe(viewLifecycleOwner) { count ->
            val todoDoneItem = getString(R.string.number_of_done_todo) + count
            binding.numberOfDoneTodo.text = todoDoneItem
        }
        lifecycleScope.launch {
            viewModel.data.collectLatest {
                updateUI(it)
            }
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
                requireActivity().supportFragmentManager.beginTransaction()
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
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, AddEditTodoItemFragment.newInstance(MODE_ADD))
            .addToBackStack(null).commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }
}