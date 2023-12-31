package com.example.mytodoapp.presentation.featureTodoList


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.MainActivity
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
import com.example.mytodoapp.presentation.featureAddEditTodoItem.DatePickerHelper
import com.example.mytodoapp.presentation.theme.AppTheme
import com.example.mytodoapp.presentation.theme.LocalMyColors
import com.example.mytodoapp.presentation.theme.LocalMyTypography
import javax.inject.Inject

/**
 * This class represents a fragment that displays the main todo list in an Android application.
 * It handles the user interface elements and logic for viewing, editing, and managing todo items.
 * The class follows the single responsibility principle by focusing on the specific task of managing
 * the main todo list.
 */

class MainTodoListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private val datePickerHelper by lazy {
        DatePickerHelper(requireActivity() as AppCompatActivity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent
            .mainTodoListComponent()
            .create()
            .inject(this)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]
        viewModel.initLifecycleOwner(viewLifecycleOwner)
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme() {
                    MainTodoListScreen(viewModel)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainTodoListFragment()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun MainTodoListScreen(viewModel: MainViewModel) {
        AppTheme() {
            val todoItems by viewModel.data.collectAsState(mutableListOf())
            val doneTodoCount by viewModel.doneTodoCount.collectAsState()
            val showThemeMenu = remember { mutableStateOf(false) }
            val selectedThemeMode = remember { mutableStateOf(AppCompatDelegate.MODE_NIGHT_NO) }

            if (showThemeMenu.value) {
                ThemeMenuPopup(
                    onCloseMenu = { showThemeMenu.value = false },
                    onThemeSelected = { themeMode ->
                        selectedThemeMode.value = themeMode
                        showThemeMenu.value = false
                        val act = requireActivity() as MainActivity
                        act.updateAppTheme(selectedThemeMode.value)
                    }
                )
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = LocalMyColors.current.colorBackPrimary,
                        modifier = Modifier.height(165.dp)
                    ) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column(
                                modifier = Modifier.padding(start = 58.dp, bottom = 20.dp),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = stringResource(R.string.my_todo_items),
                                    style = LocalMyTypography.current.h1,
                                    color = LocalMyColors.current.colorPrimary
                                )
                                Text(
                                    text = "${stringResource(R.string.number_of_done_todo)} $doneTodoCount",
                                    style = LocalMyTypography.current.subtitle1,
                                    color = LocalMyColors.current.colorTertiary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            Column(
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                            {

                                IconButton(
                                    onClick = { viewModel.changeMode() },

                                    ) {
                                    val icon = if (viewModel.showDoneItems) {
                                        painterResource(R.drawable.ic_visible)
                                    } else {
                                        painterResource(R.drawable.ic_invisible)
                                    }

                                    Icon(
                                        painter = icon,
                                        contentDescription = stringResource(R.string.item_info),
                                        tint = LocalMyColors.current.colorBlue
                                    )
                                }
                            }
                        }
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .background(color = LocalMyColors.current.colorBackElevated),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(color = LocalMyColors.current.colorBackElevated)
                        ) {
                            TodoList(todoItems, viewModel)
                            IconButton(
                                onClick = { showThemeMenu.value = true },
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                                    .size(56.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_theme),
                                    contentDescription = stringResource(R.string.change_theme),
                                    tint = LocalMyColors.current.colorBlue
                                )
                            }
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            openAddEditFrag(AddEditTodoItemFragment.MODE_ADD)
                        },
                        backgroundColor = LocalMyColors.current.colorBlue
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_new_to_do_item),
                            tint = Color.White
                        )
                    }
                }
            )
        }
    }

    @Composable
    fun ThemeMenuPopup(
        onCloseMenu: () -> Unit,
        onThemeSelected: (Int) -> Unit
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { onCloseMenu() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_NO)
                    onCloseMenu()
                }
            ) {
                Text(stringResource(R.string.light_theme))
            }
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_YES)
                    onCloseMenu()
                }
            ) {
                Text(stringResource(R.string.dark_theme))
            }
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    onCloseMenu()
                }
            ) {
                Text(stringResource(R.string.system_theme))
            }
        }
    }

    @Composable
    fun TodoList(todoItems: MutableList<TodoItem>, viewModel: MainViewModel) {
        AppTheme() {
            val filteredItems = if (viewModel.showDoneItems) {
                todoItems.sortedBy { it.creationDate }
            } else {
                todoItems.filter { !it.done }.sortedBy { it.creationDate }
            }
            LazyColumn {
                items(filteredItems) { todoItem ->
                    TodoItemRow(todoItem)
                }
            }
        }
    }

    @Preview("Light Theme", showBackground = true)
    @Composable
    fun MainTodoListScreenLightPreview() {
        AppTheme {
            MainTodoListScreen(viewModel = viewModel)
        }
    }

    @Preview("Dark Theme", showBackground = true)
    @Composable
    fun MainTodoListScreenDarkPreview() {
        AppTheme(darkTheme = true) {
            MainTodoListScreen(viewModel = viewModel)
        }
    }

    private fun openAddEditFrag(param: String) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_to_right
            )
            .replace(
                R.id.rootContainer,
                AddEditTodoItemFragment.newInstance(param)
            )
            .addToBackStack(null)
            .commit()
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TodoItemRow(todoItem: TodoItem) {
        AppTheme() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 0.dp,
                onClick = {
                    openAddEditFrag(todoItem.id)
                },
                shape = RoundedCornerShape(8.dp),
                backgroundColor = LocalMyColors.current.colorBackElevated,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todoItem.done,
                        onCheckedChange = { isChecked ->
                            viewModel.changeEnableState(todoItem)
                        },
                        modifier = Modifier
                            .padding(start = 12.dp)

                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    if (todoItem.priority != Importance.NORMAL) {
                        Image(
                            painter = painterResource(
                                id = if (todoItem.priority == Importance.HIGH) {
                                    R.drawable.ic_high_priority
                                } else R.drawable.ic_low_priority
                            ),
                            contentDescription = stringResource(R.string.priority),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 2.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = todoItem.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = if (todoItem.done) {
                                LocalMyTypography.current.body2.copy(textDecoration = TextDecoration.LineThrough)
                            } else {
                                LocalMyTypography.current.body2
                            },
                            color = LocalMyColors.current.colorPrimary,
                            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp)
                        )
                        if (todoItem.deadline != null) {
                            Text(
                                text = datePickerHelper.formatDateString(todoItem.deadline!!),
                                style = LocalMyTypography.current.body2,
                                color = LocalMyColors.current.colorGray,
                                modifier = Modifier.padding(start = 3.dp, top = 2.dp, bottom = 1.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            parentFragmentManager.beginTransaction()
                                .replace(
                                    R.id.rootContainer,
                                    AddEditTodoItemFragment.newInstance(todoItem.id)
                                )
                                .addToBackStack(null)
                                .commit()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_info),
                            contentDescription = stringResource(R.string.item_info)
                        )
                    }
                }
            }
        }
    }
}