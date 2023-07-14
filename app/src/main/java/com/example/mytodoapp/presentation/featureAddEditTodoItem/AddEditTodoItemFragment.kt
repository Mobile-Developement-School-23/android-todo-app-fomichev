package com.example.mytodoapp.presentation.featureAddEditTodoItem


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.theme.LocalMyColors
import com.example.mytodoapp.presentation.theme.LocalMyTypography
import com.example.mytodoapp.presentation.theme.AppTheme
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureTodoList.MainTodoListFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private val datePickerHelper by lazy {
        DatePickerHelper(requireActivity() as AppCompatActivity)
    }
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
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[TodoItemViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme() {
                    AddEditTodoItemScreen()
                }
            }
        }

    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
    @Composable
    fun AddEditTodoItemScreen() {
        var isSwitchOn by remember { mutableStateOf(false) }
        val isEditMode = (todoItemId != MODE_ADD)
        if (todoItemId != MODE_ADD) viewModel.getTodoItem(todoItemId)
        val (description, setDescription) = remember { mutableStateOf("") }
        val (creatingDate, setCreatingDate) = remember { mutableStateOf(Date(System.currentTimeMillis())) }

        val (id, setId) = remember { mutableStateOf(MODE_ADD) }
        val (priority, setPriority) = remember { mutableStateOf(Importance.NORMAL) }
        var (deadline, setDeadline) = remember { mutableStateOf(Date.valueOf("1980-01-01")) }
        val (itemDone, setItemDone) = remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        var deleteJob: Job? = null
        var showUndoSnackbar by remember { mutableStateOf(false) }
        var countdown by remember { mutableStateOf(5) }
        var isDeleteInProgress by remember { mutableStateOf(false) }
        var formattedDate by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            if (isEditMode) {
                viewModel.getTodoItem(todoItemId)
            }
        }


        LaunchedEffect(viewModel.todoItem) {
            viewModel.todoItem.collect { todoItem ->
                if (isEditMode && todoItem != null) {
                    setDescription(todoItem.description)
                    setPriority(todoItem.priority)
                    setItemDone(todoItem.done)
                    setCreatingDate(todoItem.creationDate)
                    setId(todoItem.id)
                    todoItem.deadline?.let {
                        setDeadline(it)
                        formattedDate = datePickerHelper.formatDateString(it)
                    }
                }
            }
        }
        LaunchedEffect(deadline) {
            isSwitchOn = (deadline != Date.valueOf("1980-01-01"))
        }
        val maxLength = 15
        val shortenedDescription = if (description.length > maxLength) {
            description.substring(0, maxLength) + "..."
        } else {
            description
        }
        fun cancelDelete() {
            isDeleteInProgress = false
            showUndoSnackbar = false
        }

        fun deleteTodoItem() {
            viewModel.getTodoItem(todoItemId)
            val todoDelete = viewModel.todoItem.value

            deleteJob?.cancel()
            deleteJob = coroutineScope.launch {
                showUndoSnackbar = true
                countdown = 5
                isDeleteInProgress = true

                repeat(5) {
                    delay(1000)
                    countdown--
                    if (!isDeleteInProgress) {
                        return@launch
                    }
                }

                if (isDeleteInProgress) {
                    viewModel.deleteTodoItem(todoDelete!!)
                    isDeleteInProgress = false
                    openMainFrag()
                }
            }
        }


        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    backgroundColor = LocalMyColors.current.colorBackPrimary,
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            openMainFrag()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.my_todo_items),
                                tint = LocalMyColors.current.colorPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (isEditMode) editTodoItem(
                                description,
                                priority,
                                itemDone,
                                deadline,
                                creatingDate,
                                id
                            )
                            else saveTodoItem(description, priority, false, deadline)
                        }) {
                            Text(
                                text = stringResource(id = R.string.save),
                                style = LocalMyTypography.current.body2,
                                color = LocalMyColors.current.colorBlue,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = LocalMyColors.current.colorBackPrimary)
                )
                {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            backgroundColor = LocalMyColors.current.colorBackSecondary,
                            shape = RoundedCornerShape(8.dp),
                            elevation = 4.dp
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                value = description,
                                onValueChange = { setDescription(it) },
                                textStyle = LocalTextStyle.current.copy(
                                    color = LocalMyColors.current.colorPrimary
                                ),
                                singleLine = false,
                                maxLines = Int.MAX_VALUE,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = LocalMyColors.current.colorBackSecondary,
                                    cursorColor = MaterialTheme.colors.onSurface,
                                    focusedIndicatorColor = LocalMyColors.current.colorBackSecondary,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        Column {
                            Text(
                                text = when (priority) {
                                    Importance.LOW -> stringResource(id = R.string.low_priority)
                                    Importance.HIGH -> stringResource(id = R.string.high_priority)
                                    else -> stringResource(id = R.string.normal_priority)
                                },
                                color = LocalMyColors.current.colorPrimary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                PriorityItems(
                                    priority = priority,
                                    onPrioritySelected = {
                                        setPriority(it)
                                        expanded = false
                                    }
                                )
                            }
                        }




                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(0.3f)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.do_for),
                                    color = LocalMyColors.current.colorPrimary,
                                    style = LocalMyTypography.current.body2
                                )
                                if (isSwitchOn) {
                                    Text(
                                        text = formattedDate,
                                        color = LocalMyColors.current.colorPrimary,
                                        style = LocalMyTypography.current.body2
                                    )
                                }
                            }

                            Switch(
                                checked = isSwitchOn,
                                onCheckedChange = { isChecked ->
                                    isSwitchOn = isChecked
                                    if (isChecked) {
                                        datePickerHelper.showDatePicker { selectedDate ->
                                            deadline = selectedDate
                                             formattedDate = datePickerHelper.formatDateString(selectedDate)
                                        }
                                    } else {
                                        deadline = Date.valueOf("1980-01-01")
                                    }
                                },
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = {
                                    if (isEditMode) deleteTodoItem()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.delete),
                                    tint = MaterialTheme.colors.error
                                )
                            }

                            Text(
                                text = stringResource(id = R.string.delete),
                                style = LocalMyTypography.current.subtitle1,
                                color = LocalMyColors.current.colorRed,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            if (showUndoSnackbar) {
                                Snackbar(
                                    modifier = Modifier.padding(16.dp),
                                    action = {
                                        TextButton(onClick = { cancelDelete() }) {
                                            Text(text = stringResource(R.string.cancel_snackbar), color = LocalMyColors.current.colorRed)
                                        }
                                    },
                                    backgroundColor = LocalMyColors.current.colorGray
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "Удалить ${shortenedDescription}?",
                                            style = LocalMyTypography.current.body2,
                                            color = LocalMyColors.current.colorWhite,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                        Text(
                                            text = countdown.toString(),
                                            style = LocalMyTypography.current.body1,
                                            color = LocalMyColors.current.colorWhite,
                                                    modifier = Modifier
                                                        .padding(vertical = 4.dp)
                                                        .padding(start = 18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        )
    }


    @Preview("Light Theme", showBackground = true)
    @Composable
    fun AddEditTodoItemScreenLightPreview() {
        AppTheme {
            AddEditTodoItemScreen()

        }
    }

    @Preview("Dark Theme", showBackground = true)
    @Composable
    fun AddEditTodoItemScreenDarkPreview() {
        AppTheme(darkTheme = true) {
            AddEditTodoItemScreen()
        }
    }

    private fun openMainFrag() {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_to_right
            )
            .replace(
                R.id.rootContainer,
                MainTodoListFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
    }

    private fun editTodoItem(
        description: String?,
        priority: Importance,
        itemDone: Boolean,
        deadline: Date?,
        creatingDate: Date,
        id: String
    ) {
        val inputDescription = description?.trim()
        val priority = priority
        val done = itemDone
        val creatingDate = creatingDate
        val changeDate = Date(System.currentTimeMillis())
        val deadline = if (deadline == Date.valueOf("1980-01-01")) null else deadline
        val id = id

        viewModel.editTodoItem(
            inputDescription,
            priority,
            done,
            creatingDate,
            changeDate,
            deadline,
            id
        )
        openMainFrag()
    }


    private fun saveTodoItem(
        description: String?,
        priority: Importance,
        itemDone: Boolean,
        deadline: Date?,
        id: String = MODE_ADD
    ) {
        val inputDescription = description?.trim()
        val priority = priority
        val done = itemDone
        val creatingDate = Date(System.currentTimeMillis())
        val changeDate = Date(System.currentTimeMillis())
        val deadline = if (deadline == Date.valueOf("1980-01-01")) null else deadline
        val id = UUID.randomUUID().toString()

        viewModel.addTodoItem(
            inputDescription,
            priority,
            done,
            creatingDate,
            changeDate,
            deadline,
            id
        )
        openMainFrag()
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





@Composable
fun PriorityItems(
    priority: Importance,
    onPrioritySelected: (Importance) -> Unit
) {
    val priorityItems = listOf(
        Pair(Importance.LOW, stringResource(id = R.string.low_priority)),
        Pair(Importance.NORMAL, stringResource(id = R.string.normal_priority)),
        Pair(Importance.HIGH, stringResource(id = R.string.high_priority))
    )

    priorityItems.forEach { (itemPriority, itemText) ->
        DropdownMenuItem(
            onClick = { onPrioritySelected(itemPriority) }
        ) {
            Text(
                text = itemText,
                style = LocalMyTypography.current.body2
            )
        }
    }
}
}




