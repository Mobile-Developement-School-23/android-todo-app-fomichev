package com.example.mytodoapp.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mytodoapp.R
import com.example.mytodoapp.TodoItem
import com.example.mytodoapp.TodoItem.Companion.HIGH_IMPORTANCE
import com.example.mytodoapp.TodoItem.Companion.LOW_IMPORTANCE
import com.example.mytodoapp.TodoItem.Companion.NORMAL_IMPORTANCE
import com.example.mytodoapp.TodoItem.Companion.NO_DEADLINE
import com.example.mytodoapp.TodoItemsRepository
import com.example.mytodoapp.TodoItemsRepository.Companion.idGenerate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddEditTodoItemFragment : Fragment() {
    private var param1: Int? = null
    private val todoItemRepository = TodoItemsRepository()
    private lateinit var tvCalendar: TextView
    private lateinit var editTextDescription: EditText
    private lateinit var saveButton: Button
    private lateinit var ibDeleteItem: ImageButton
    private lateinit var ibCancel: ImageButton
    private lateinit var spinnerPriority: Spinner
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchCalendar: Switch
    var deadlineItem: String = NO_DEADLINE
    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("dd-MMMM-yyyy")
    private var creationDate = NO_CREATION_DATE
    var itemDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_todo_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        chooseLaunchMode()
    }

    private fun initViews(view: View) {
        tvCalendar = view.findViewById(R.id.tvCalendar)
        switchCalendar = view.findViewById(R.id.switchCalendar)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        spinnerPriority = view.findViewById(R.id.spinner_important)
        saveButton = view.findViewById(R.id.btSave)
        ibDeleteItem = view.findViewById(R.id.ibDeleteItem)
        ibCancel = view.findViewById(R.id.ibCancel)
        initSpinnerPriority()
    }

    private fun chooseLaunchMode() {
        if (param1 == null) throw RuntimeException("Param screen mode is absent")
        else if (param1 != MODE_ADD) launchEditMode()
        else launchAddMode()
    }

    private fun launchEditMode() {
        fillTheFields()

        saveButton.setOnClickListener {
            saveEditTodoItem()
        }
        ibDeleteItem.setOnClickListener {
            val exTodoItem = param1?.let { todoItemRepository.getToDoItemById(it) }
            todoItemRepository.deleteToDoItem(exTodoItem!!)
            openMainTodoListFragment()
        }
        ibCancel.setOnClickListener {
            openMainTodoListFragment()
        }
    }

    private fun fillTheFields() {
        val exTodoItem = todoItemRepository.getToDoItemById(param1!!)
        if (exTodoItem == null) {
            throw RuntimeException("TodoItem not exist in repository")
        } else {
            editTextDescription.setText(exTodoItem.description)

            when (exTodoItem.priority) {
                HIGH_IMPORTANCE -> spinnerPriority.setSelection(2)
                LOW_IMPORTANCE -> spinnerPriority.setSelection(1)
                else -> spinnerPriority.setSelection(0)
            }

            if (exTodoItem.deadline != NO_DEADLINE) {
                tvCalendar.text = exTodoItem.deadline
                tvCalendar.visibility = View.VISIBLE
                switchCalendar.isChecked = true
                deadlineItem = exTodoItem.deadline

            }
            itemDone = exTodoItem.done
            creationDate = exTodoItem.creationDate
            initSwitchCalendar()
        }
    }

    private fun saveEditTodoItem() {
        val editItemPriority =
            if (spinnerPriority.selectedItemId.toInt() == 1) LOW_IMPORTANCE
            else if (spinnerPriority.selectedItemId.toInt() == 2) HIGH_IMPORTANCE
            else NORMAL_IMPORTANCE
        var deadlineTodoItem = NO_DEADLINE
        if (switchCalendar.isChecked) deadlineTodoItem = deadlineItem

        val changeDate = simpleDateFormat.format(Date())

        if (editTextDescription.text?.isNotEmpty() == true) {
            val todoItem = TodoItem(
                "$param1",
                "${editTextDescription.text}",
                editItemPriority,
                itemDone,
                creationDate,
                changeDate,
                deadlineTodoItem
            )
            todoItemRepository.editToDoItem(todoItem)
            openMainTodoListFragment()
        } else {
            Toast.makeText(
                activity,
                R.string.toast_for_empty_edit_text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun launchAddMode() {
        initSwitchCalendar()
        saveButton.setOnClickListener {
            val idItem = idGenerate
            val itemPriority =
                if (spinnerPriority.selectedItemId.toInt() == 1) LOW_IMPORTANCE
                else if (spinnerPriority.selectedItemId.toInt() == 2) HIGH_IMPORTANCE
                else NORMAL_IMPORTANCE
            val currentDate = simpleDateFormat.format(Date())
            if (editTextDescription.text?.isNotEmpty() == true) {
                val todoItem = TodoItem(
                    "$idItem",
                    "${editTextDescription.text}",
                    itemPriority,
                    false,
                    currentDate,
                    currentDate,
                    deadlineItem
                )
                todoItemRepository.addToDoItem(todoItem)
                openMainTodoListFragment()
            } else Toast.makeText(
                activity,
                getString(R.string.toast_for_empty_edit_text),
                Toast.LENGTH_SHORT
            ).show()

        }
        ibDeleteItem.setOnClickListener {
            openMainTodoListFragment()
        }

        ibCancel.setOnClickListener {
            openMainTodoListFragment()
        }
    }

    private fun openMainTodoListFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.root_container, MainTodoListFragment.newInstance())
            .commit()
    }

    private fun initSwitchCalendar() {
        switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setData()
                tvCalendar.visibility = View.VISIBLE
            } else {
                tvCalendar.visibility = View.GONE
            }
        }
    }

    private fun initSpinnerPriority() {
        ArrayAdapter.createFromResource(
            activity as AppCompatActivity,
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPriority.adapter = adapter
        }
    }

    private fun setData() {
        val datePicker = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                datePicker[Calendar.YEAR] = year
                datePicker[Calendar.MONTH] = month
                datePicker[Calendar.DAY_OF_MONTH] = dayOfMonth
                val dateFormat = "dd-MMMM-yyyy"
                val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
                val tvCalendar = activity?.findViewById<TextView>(R.id.tvCalendar)
                tvCalendar?.text = simpleDateFormat.format(datePicker.time)
                deadlineItem = simpleDateFormat.format(datePicker.time)
            }
        DatePickerDialog(
            requireActivity(),
            R.style.MyDatePickerDialog,
            date,
            datePicker[Calendar.YEAR],
            datePicker[Calendar.MONTH],
            datePicker[Calendar.DAY_OF_MONTH]
        ).show()
    }


    companion object {

        const val MODE_ADD = -1
        private const val ARG_PARAM1 = "param1"
        private const val NO_CREATION_DATE = ""

        @JvmStatic
        fun newInstance(param1: Int?) = AddEditTodoItemFragment().apply {
            arguments = Bundle().apply {
                if (param1 != null) {
                    putInt(ARG_PARAM1, param1)
                }
            }
        }
    }
}