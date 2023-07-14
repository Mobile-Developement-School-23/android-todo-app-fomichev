package com.example.mytodoapp.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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


val LocalMyColors = staticCompositionLocalOf<MyColors> {
    error("No colors provided")
}

val LocalMyTypography = staticCompositionLocalOf<androidx.compose.material.Typography> {
    error("No fonts provided")
}