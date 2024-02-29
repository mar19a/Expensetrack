package com.example.expensetrack.ui.theme

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorText(error: String) {
    Text(
        error,
        style = Theme.typography.overline.copy(
            color = Theme.colors.error,
        )
    )
}