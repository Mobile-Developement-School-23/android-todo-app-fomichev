package com.example.mytodoapp.presentation.featureAddEditTodoItem

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.example.mytodoapp.R
import com.example.mytodoapp.databinding.FragmentAddEditTodoItemBinding
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import java.sql.Date
import java.util.Calendar
import java.util.UUID

class AddEditFragmentViewController(
    private val fragment: AddEditTodoItemFragment,
    private val binding: FragmentAddEditTodoItemBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoItemViewModel,
    private val todoItemId: String
) {

    private val datePickerHelper by lazy {
        DatePickerHelper(fragment.context as AppCompatActivity)
    }
    private val fragManager = fragment.requireActivity().supportFragmentManager
    private var isCalendarVisible = false
    private var deadlineItem: Date? = TodoItem.NO_DEADLINE
    private var creationDate = NO_CREATION_DATE
    private var itemDone = false


    fun chooseLaunchMode(todoItemId: String) {
        initSpinnerPriority()
        when (todoItemId) {
            MODE_ADD -> launchAddMode()
            else -> launchEditMode()
        }

    }

    private fun launchEditMode() = with(binding) {
        viewModel.getTodoItem(todoItemId)
        if (deadlineItem == TodoItem.NO_DEADLINE) initSwitchCalendar()
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
            viewModel.editTodoItem(
                "${editTextDescription.text}", itemPriority,
                itemDone, currentDate, currentDate, deadlineItem, todoItemId
            )
            openMainTodoListFragment()
        } else Toast.makeText(
            fragment.context,
            R.string.toast_for_empty_edit_text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initEditTodoItem() = with(binding) {
        viewModel.todoItem.observe(lifecycleOwner) {
            editTextDescription.setText(it.description)
            setPrioritySpinnerSelection(it.priority)
            if (it.deadline != TodoItem.NO_DEADLINE) {
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
        val id = UUID.randomUUID().toString()
        if (editTextDescription.text?.isNotEmpty() == true) {
            viewModel.addTodoItem(
                "${editTextDescription.text}", itemPriority,
                false, currentDate, currentDate, deadlineItem, id
            )
            openMainTodoListFragment()
        } else Toast.makeText(
            fragment.context,
            R.string.toast_for_empty_edit_text,
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
                deadlineItem = TodoItem.NO_DEADLINE
            }
        }
    }

    private fun initSpinnerPriority() = with(binding) {
        ArrayAdapter.createFromResource(
            fragment.requireActivity() as AppCompatActivity,
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
        fragManager.beginTransaction()
            .replace(R.id.rootContainer, MainTodoListFragment.newInstance())
            .commit()
    }

    companion object {
        private const val NO_CREATION_DATE = ""
        const val MODE_ADD = "-1"
    }

}