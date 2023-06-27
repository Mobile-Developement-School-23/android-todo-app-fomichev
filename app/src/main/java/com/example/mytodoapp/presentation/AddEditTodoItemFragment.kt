package com.example.mytodoapp.presentation

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.domain.TodoItem.Companion.HIGH_IMPORTANCE
import com.example.mytodoapp.domain.TodoItem.Companion.LOW_IMPORTANCE
import com.example.mytodoapp.domain.TodoItem.Companion.NORMAL_IMPORTANCE
import com.example.mytodoapp.domain.TodoItem.Companion.NO_DEADLINE

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddEditTodoItemFragment : Fragment() {

    private lateinit var viewModel: TodoItemViewModel
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
    private var todoItemId: Int = TodoItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            todoItemId = it.getInt(ARG_PARAM1)
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
        viewModel = ViewModelProvider(this)[TodoItemViewModel::class.java]
        initViews(view)
        when (todoItemId) {
            MODE_ADD -> launchAddMode()
            else -> launchEditMode()
        }
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

    private fun launchEditMode() {
        viewModel.getTodoItem(todoItemId)
        viewModel.todoItem.observe(viewLifecycleOwner) {
            editTextDescription.setText(it.description)
            when (it.priority) {
               HIGH_IMPORTANCE -> spinnerPriority.setSelection(2)
               LOW_IMPORTANCE -> spinnerPriority.setSelection(1)
                else -> spinnerPriority.setSelection(0)
            }
            if (it.deadline != NO_DEADLINE) {
                tvCalendar.text = it.deadline
                tvCalendar.visibility = View.VISIBLE
                switchCalendar.isChecked = true
                deadlineItem = it.deadline

            }
            itemDone = it.done
            creationDate = it.creationDate
            initSwitchCalendar()
        }


        saveButton.setOnClickListener {
            val itemPriority =
                if (spinnerPriority.selectedItemId.toInt() == 1) LOW_IMPORTANCE
                else if (spinnerPriority.selectedItemId.toInt() == 2) HIGH_IMPORTANCE
                else NORMAL_IMPORTANCE
            val currentDate = simpleDateFormat.format(Date())
            if (editTextDescription.text?.isNotEmpty() == true) {
                viewModel.editTodoItem(
                    "${editTextDescription.text}",
                    itemPriority,
                    itemDone,
                    creationDate,
                    currentDate,
                    deadlineItem
                )
                openMainTodoListFragment()
            } else Toast.makeText(
                activity,
                getString(R.string.toast_for_empty_edit_text),
                Toast.LENGTH_SHORT
            ).show()
        }

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




    private fun launchAddMode() {
        initSwitchCalendar()
        saveButton.setOnClickListener {
            val itemPriority =
                if (spinnerPriority.selectedItemId.toInt() == 1) LOW_IMPORTANCE
                else if (spinnerPriority.selectedItemId.toInt() == 2) HIGH_IMPORTANCE
                else NORMAL_IMPORTANCE
            val currentDate = simpleDateFormat.format(Date())
            if (editTextDescription.text?.isNotEmpty() == true) {
                viewModel.addTodoItem("${editTextDescription.text}",itemPriority, false, currentDate, currentDate, deadlineItem)
                Log.d("MyLog", "${editTextDescription.text} $itemPriority ${false} $currentDate $currentDate $deadlineItem")
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
                deadlineItem = NO_DEADLINE
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