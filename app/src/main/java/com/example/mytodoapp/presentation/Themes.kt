package com.example.mytodoapp.presentation

import android.webkit.WebSettings.TextSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import java.time.format.TextStyle

data class MyColors(
    val colorSupportSeparator: Color,
    val colorSupportOverlay: Color,
    val colorPrimary: Color,
    val colorSecondary: Color,
    val colorTertiary: Color,
    val colorDisable: Color,
    val colorRed: Color,
    val colorGreen: Color,
    val colorBlue: Color,
    val colorGray: Color,
    val colorGrayLight: Color,
    val colorWhite: Color,
    val colorBackPrimary: Color,
    val colorBackSecondary: Color,
    val colorBackElevated: Color
)

data class MyTypography(
    val largeTitle: TextSize,
    val title: TextSize,
    val button: TextSize,
    val body: TextSize,
    val subhead: TextSize,
)

object MyTheme {
    val colors: MyColors
    @Composable
    get() = LocalMyColors.current

    val typography: MyTypography
        @Composable
        get() = LocalMyTypography.current
}

val LocalMyColors = staticCompositionLocalOf<MyColors> {
    error("No colors provided")
}

val LocalMyTypography = staticCompositionLocalOf<MyTypography> {
    error("No fonts provided")
}