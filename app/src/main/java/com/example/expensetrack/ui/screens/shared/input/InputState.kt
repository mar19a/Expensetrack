package com.example.expensetrack.ui.screens.shared.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation

val DefaultInputColor = Color.DarkGray
fun Color.content() = copy(alpha = .8f)

data class InputState(
    val value: String,
    val onValueChanged: (String) -> Unit = {},
    val color: Color = DefaultInputColor,
    val icon: Pair<Alignment.Horizontal, ImageVector>? = null,
    val error: String? = null,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val readOnly: Boolean = false,
    val enabled: Boolean = true,
    val singleLine: Boolean = true,
    val required: Boolean = false,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
)