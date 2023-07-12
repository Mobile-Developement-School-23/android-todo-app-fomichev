package com.example.mytodoapp.presentation.featureAddEditTodoItem

import androidx.compose.runtime.getValue
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import org.w3c.dom.Text
import java.sql.Date
import java.util.UUID
import javax.inject.Inject

/**
 * This class represents a fragment responsible for adding or editing a todo item in an Android application.
 * It handles the user interface elements and logic for creating, editing, deleting and saving todo items.
 * The class follows the single responsibility principle by focusing on the specific task of managing todo items.
 */

class AddEditTodoItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: TodoItemViewModel
    private var todoItemId: String = TodoItem.UNDEFINED_ID
    var todoItemCopy: TodoItem? = null
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[TodoItemViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                AddEditTodoItemScreen()
            }
        }

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun AddEditTodoItemScreen() {
       // val todoItemState = viewModel.todoItem.value
        if (todoItemId != MODE_ADD) viewModel.getTodoItem(todoItemId)
        val (description, setDescription) = remember { mutableStateOf("") }
        val (priority, setPriority) = remember { mutableStateOf(Importance.NORMAL) }
        val (deadline, setDeadline) = remember { mutableStateOf(TodoItem.NO_DEADLINE) }
        val (itemDone, setItemDone) = remember { mutableStateOf(false) }


        val isEditMode = (todoItemId != MODE_ADD)


        LaunchedEffect(Unit) {
            if (isEditMode) {
                viewModel.todoItem.observe(viewLifecycleOwner, object : Observer<TodoItem?> {
                    override fun onChanged(todoItem: TodoItem?) {
                        if (todoItem != null) {
                            todoItemCopy = todoItem.copy()

                            setDescription(todoItemCopy!!.description)
                            setPriority(todoItemCopy!!.priority)
                            setItemDone(todoItemCopy!!.done)

                            viewModel.todoItem.removeObserver(this)
                        }
                    }
                })
            }

        }


        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isEditMode) {
                                stringResource(R.string.my_todo_items)
                            } else {
                                stringResource(R.string.my_todo_items)
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { requireActivity().onBackPressed() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.my_todo_items)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { saveTodoItem(description, priority, false, deadline) }) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = stringResource(R.string.my_todo_items)
                            )
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        onValueChange = { setDescription(it) },
                        label = { Text(stringResource(R.string.what_need_to_do)) },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            cursorColor = MaterialTheme.colors.onSurface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrioritySelector(
                        priority = priority,
                        onPrioritySelected = { setPriority(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    deadline?.let { it1 ->
                        DeadlineSwitch(
                            isEditMode = isEditMode,
                            deadline = it1,
                            onDeadlineChanged = {  }
                        )
                    }
                    if (isEditMode) {
                        DeleteButton(
                            onDeleteClicked = {
                              viewModel.deleteTodoItem(todoItemCopy!!)
                                requireActivity().onBackPressed()
                            }
                        )
                    }
                }
            }
        )
    }

    private fun saveTodoItem(description: String?, priority: Importance, itemDone: Boolean,
                             deadline: Date?, id: String = MODE_ADD) {
        val inputDescription = description?.trim()
        val priority = priority
        val done = itemDone
        val creatingDate = Date(System.currentTimeMillis())
        val changeDate = Date(System.currentTimeMillis())
        val deadline = deadline
        val id = UUID.randomUUID().toString()

        viewModel.addTodoItem(inputDescription, priority, done, creatingDate, changeDate, deadline, id)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, MainTodoListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val MODE_ADD = "-1"
        private const val ARG_PARAM1 = "param1"

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

@Composable
fun PrioritySelector(
    priority: Importance,
    onPrioritySelected: (Importance) -> Unit
) {
    val priorityItems = listOf(
        Pair(Importance.LOW, stringResource(R.string.low_priority)),
        Pair(Importance.NORMAL, stringResource(R.string.normal_priority)),
        Pair(Importance.HIGH, stringResource(R.string.high_priority))
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(R.string.priority),
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.subtitle1
        )

        DropdownMenu(
            modifier = Modifier.align(Alignment.CenterEnd),
            expanded = false,
            onDismissRequest = { },
            content = {
                priorityItems.forEach { (itemPriority, itemText) ->
                    DropdownMenuItem(
                        onClick = {
                            onPrioritySelected(itemPriority)
                        }
                    ) {
                        Text(text = itemText)
                    }
                }
            })
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { }
            ) {
                Text(text = priority.toString())
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
    }


@Composable
fun DeadlineSwitch(
    isEditMode: Boolean,
    deadline: Date,
    onDeadlineChanged: (Date) -> Unit
) {
    var isSwitchOn by remember { mutableStateOf(isEditMode && deadline != TodoItem.NO_DEADLINE) }
    var isCalendarVisible by remember { mutableStateOf(isEditMode && deadline != TodoItem.NO_DEADLINE) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(0.3f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.do_for),
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = if (isCalendarVisible) {
                    deadline.toString()
                } else {
                    ""
                },
                modifier = Modifier.padding(top = 2.dp),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1,
                //visibility = if (isSwitchOn) {
               //     View.VISIBLE
              //  } else {
              //      View.GONE
             //   }
            )
        }

        Switch(
            checked = isSwitchOn,
            onCheckedChange = { isChecked ->
                isSwitchOn = isChecked
                isCalendarVisible = isChecked
                if (!isChecked) {
                    TodoItem.NO_DEADLINE?.let { onDeadlineChanged(it) }
                }
            },
            modifier = Modifier.padding(start = 16.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.onSurface,
                uncheckedTrackColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
fun DeleteButton(
    onDeleteClicked: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDeleteClicked) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colors.error
            )
        }

        Text(
            text = stringResource(R.string.delete),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}