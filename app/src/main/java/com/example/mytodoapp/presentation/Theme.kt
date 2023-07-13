package com.example.mytodoapp.presentation

import android.webkit.WebSettings.TextSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> blackPalette
        else -> lightPalette
    }
//    val typography = MyTypography(
//        largeTitle = TextSize(32.sp),
//        title =TextUnit(20.sp),
//        button =TextUnit(14.sp),
//        body =TextUnit(16.sp),
//        subhead =TextUnit(14.sp),
//    )


    CompositionLocalProvider(
        LocalMyColors provides colors,
       // LocalMyTypography provides typography,
        content = content
    )
}