package com.example.mytodoapp.presentation.featureAddEditTodoItem

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mytodoapp.R
import com.example.mytodoapp.domain.Importance
import com.example.mytodoapp.presentation.theme.LocalMyTypography

class AddEditViewHelper {


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