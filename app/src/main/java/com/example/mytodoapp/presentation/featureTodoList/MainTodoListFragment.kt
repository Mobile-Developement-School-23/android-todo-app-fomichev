package com.example.mytodoapp.presentation.featureTodoList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytodoapp.R
import com.example.mytodoapp.appComponent
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.domain.TodoItem
import com.example.mytodoapp.presentation.LocalMyColors
import com.example.mytodoapp.presentation.MainTheme
import com.example.mytodoapp.presentation.factory.ViewModelFactory
import com.example.mytodoapp.presentation.featureAddEditTodoItem.AddEditTodoItemFragment
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
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.initLifecycleOwner(viewLifecycleOwner)
        return ComposeView(requireContext()).apply {
            setContent {
                MainTheme() {
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
        val todoItems by viewModel.data.collectAsState(mutableListOf())
        val doneTodoCount by viewModel.doneTodoCount.collectAsState()
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
                                style = MaterialTheme.typography.h4,
                                color = LocalMyColors.current.colorPrimary

                            )
                            Text(
                                text = "${stringResource(R.string.number_of_done_todo)} $doneTodoCount",
                                style = MaterialTheme.typography.body1,
                                color = LocalMyColors.current.colorTertiary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        IconButton(
                            onClick = { viewModel.changeMode() },
                            modifier = Modifier.align(Alignment.BottomEnd)
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
            },
            content = {
                Column(
                    modifier = Modifier
                        .background(color = LocalMyColors.current.colorBackElevated),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(color = LocalMyColors.current.colorBackElevated)
                ) {
                    TodoList(todoItems, viewModel)
                }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        parentFragmentManager.beginTransaction()
                            .replace(
                                R.id.rootContainer,
                                AddEditTodoItemFragment.newInstance(AddEditTodoItemFragment.MODE_ADD)
                            )
                            .addToBackStack(null)
                            .commit()
                    },
                    backgroundColor = LocalMyColors.current.colorBlue
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_new_to_do_item)
                    )
                }
            }
        )
    }


    @Composable
    fun TodoList(todoItems: MutableList<TodoItem>, viewModel: MainViewModel)  {
        val filteredItems = if (viewModel.showDoneItems) {
            todoItems
        } else {
            todoItems.filter { !it.done }
        }


        LazyColumn {
            items(filteredItems) { todoItem ->
                TodoItemRow(todoItem)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TodoItemRow(todoItem: TodoItem) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 0.dp,
            onClick = {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootContainer, AddEditTodoItemFragment.newInstance(todoItem.id))
                    .addToBackStack(null)
                    .commit()
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
                    modifier = Modifier.padding(start = 12.dp)
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
                        style = MaterialTheme.typography.body1,
                        color = LocalMyColors.current.colorPrimary,
                        modifier = Modifier.padding(start = 3.dp, bottom = 2.dp)

                    )

                    if (todoItem.deadline != null) {
                        Text(
                            text = todoItem.deadline.toString(),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
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