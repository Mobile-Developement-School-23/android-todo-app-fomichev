package com.example.mytodoapp.presentation.featureAddEditTodoItem

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.databinding.FragmentAddEditTodoItemBinding
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItem.Companion.NO_DEADLINE
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import java.sql.Date
import java.util.Calendar
import java.util.UUID.randomUUID
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

    private val datePickerHelper by lazy {
        DatePickerHelper(requireActivity() as AppCompatActivity)
    }
    private var isCalendarVisible = false
    private var deadlineItem: Date? = NO_DEADLINE
    private var creationDate = NO_CREATION_DATE
    private var itemDone = false
    private var todoItemId: String = TodoItem.UNDEFINED_ID

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
        initSpinnerPriority()
        when (todoItemId) {
            MODE_ADD -> launchAddMode()
            else -> launchEditMode()
        }
    }

    private fun launchEditMode() = with(binding) {
        viewModel.getTodoItem(todoItemId)
        if (deadlineItem == NO_DEADLINE) initSwitchCalendar()
        initEditTodoItem()

        btSave.setOnClickListener { saveTodoItem() }

        ibDeleteItem.setOnClickListener {
            val selectedTodoItem = viewModel.todoItem.value
            if (selectedTodoItem != null) {
                viewModel.deleteTodoItem(selectedTodoItem)
                openMainTodoListFragment()
            }
        }
        ibCancel.setOnClickListener {
            openMainTodoListFragment()
        }
    }

    private fun saveTodoItem() = with(binding) {
        val itemPriority =
            if (spinnerPriority.selectedItemId.toInt() == 1) Importance.LOW
            else if (spinnerPriority.selectedItemId.toInt() == 2) Importance.HIGH
            else Importance.NORMAL
        val currentDate = Date(System.currentTimeMillis())

        if (editTextDescription.text?.isNotEmpty() == true) {
            viewModel.editTodoItem("${editTextDescription.text}", itemPriority,
                itemDone, currentDate, currentDate, deadlineItem, todoItemId
            )
            openMainTodoListFragment()
        } else Toast.makeText(
            activity,
            getString(R.string.toast_for_empty_edit_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initEditTodoItem() = with(binding) {
        viewModel.todoItem.observe(viewLifecycleOwner) {
            editTextDescription.setText(it.description)
            setPrioritySpinnerSelection(it.priority)
            if (it.deadline != NO_DEADLINE) {
                tvCalendar.text = it.deadline.toString()
                tvCalendar.visibility = View.VISIBLE
                switchCalendar.isChecked = true
                deadlineItem = it.deadline
            } else {
                tvCalendar.visibility = View.GONE
                switchCalendar.isChecked = false
            }
            itemDone = it.done
            creationDate = it.creationDate.toString()
            initSwitchCalendar()
        }
    }

    private fun setPrioritySpinnerSelection(priority: Importance) {
        val prioritySelection = when (priority) {
            Importance.HIGH -> 2
            Importance.LOW -> 1
            else -> 0
        }
        binding.spinnerPriority.setSelection(prioritySelection)
    }

    private fun launchAddMode() = with(binding) {
        isCalendarVisible = true
        initSwitchCalendar()
        btSave.setOnClickListener { saveAddTodoItem() }
        ibDeleteItem.setOnClickListener { openMainTodoListFragment() }
        ibCancel.setOnClickListener { openMainTodoListFragment() }
    }

    private fun saveAddTodoItem() = with(binding) {
        val itemPriority =
            if (spinnerPriority.selectedItemId.toInt() == 1) Importance.LOW
            else if (spinnerPriority.selectedItemId.toInt() == 2) Importance.HIGH
            else Importance.NORMAL
        val currentDate = Date(System.currentTimeMillis())
        val id = randomUUID().toString()
        if (editTextDescription.text?.isNotEmpty() == true) {
            viewModel.addTodoItem("${editTextDescription.text}", itemPriority,
                false, currentDate, currentDate, deadlineItem, id
            )
            openMainTodoListFragment()
        } else Toast.makeText(
            activity,
            getString(R.string.toast_for_empty_edit_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initSwitchCalendar() = with(binding) {
        switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setData()
                tvCalendar.visibility = View.VISIBLE
            } else {
                isCalendarVisible = true
                tvCalendar.visibility = View.GONE
                deadlineItem = NO_DEADLINE
            }
        }
    }

    private fun initSpinnerPriority() = with(binding) {
        ArrayAdapter.createFromResource(
            activity as AppCompatActivity,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPriority.adapter = adapter
        }
    }

    private fun setData() = with(binding) {
        val datePicker = Calendar.getInstance()
        datePickerHelper.setData(tvCalendar, datePicker, isCalendarVisible) { selectedDate ->
            deadlineItem = selectedDate
        }
    }

    private fun openMainTodoListFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, MainTodoListFragment.newInstance())
            .commit()
    }

    companion object {

        const val MODE_ADD = "-1"
        private const val ARG_PARAM1 = "param1"
        private const val NO_CREATION_DATE = ""

        @JvmStatic
        fun newInstance(param1: String?) = AddEditTodoItemFragment().apply {
            arguments = Bundle().apply {
                if (param1 != null) { putString(ARG_PARAM1, param1) }
            }
        }
    }
}