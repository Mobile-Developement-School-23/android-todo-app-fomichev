package com.example.mytodoapp.presentation.featureAddEditTodoItem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.databinding.FragmentAddEditTodoItemBinding
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import javax.inject.Inject

/**
 * This class represents a fragment responsible for adding or editing a todo item in an Android application.
 * It handles the user interface elements and logic for creating, editing, deleting and saving todo items.
 * The class follows the single responsibility principle by focusing on the specific task of managing todo items.
 */
class AddEditTodoItemFragment : Fragment() {

    private lateinit var binding: FragmentAddEditTodoItemBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: TodoItemViewModel


    private var todoItemId: String = TodoItem.UNDEFINED_ID
    private var fragmentViewComponent: AddEditFragmentViewController? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent
            .addEditTodoItemComponent()
            .create()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItemId = it.getString(ARG_PARAM1)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditTodoItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[TodoItemViewModel::class.java]
        fragmentViewComponent = AddEditFragmentViewController(
            this,
            binding,
            viewLifecycleOwner,
            viewModel,
            todoItemId
        ).apply {
            initSpinnerPriority()
            when (todoItemId) {
                MODE_ADD -> launchAddMode()
                else -> launchEditMode()
            }
        }
    }

    companion object {

        private const val ARG_PARAM1 = "param1"
        const val MODE_ADD = "-1"

        @JvmStatic
        fun newInstance(param1: String?) = AddEditTodoItemFragment().apply {
            arguments = Bundle().apply {
                if (param1 != null) {
                    putString(ARG_PARAM1, param1)
                }
            }
        }
    }
}