package com.example.mytodoapp.presentation.featureTodoList

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mytodoapp.R

class MainTodoListViewHelper {

    @Composable
    fun ThemeMenuPopup(
        onCloseMenu: () -> Unit,
        onThemeSelected: (Int) -> Unit
    ) {
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val backgroundColor = if (isSystemInDarkTheme) Color.Black else Color.White
        val textColor = if (isSystemInDarkTheme) Color.White else Color.Black

        DropdownMenu(
            expanded = true,
            onDismissRequest = { onCloseMenu() },
            modifier = Modifier
                .background(backgroundColor)
        ) {
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_NO)
                    onCloseMenu()
                },
                modifier = Modifier
                    .padding(vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.light_theme),
                    color = textColor,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_YES)
                    onCloseMenu()
                },
                modifier = Modifier
                    .padding(vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.dark_theme),
                    color = textColor,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }
            DropdownMenuItem(
                onClick = {
                    onThemeSelected(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    onCloseMenu()
                },
                modifier = Modifier
                    .padding(vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.system_theme),
                    color = textColor,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}