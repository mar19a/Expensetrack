package com.example.expensetrack.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

typealias Theme = MaterialTheme

@Composable
fun ExpenseManagerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = ColorScheme,
        shapes = Shapes,
        content = content
    )
}