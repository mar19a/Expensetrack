package com.example.expensetrack.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.example.expensetrack.ui.domain.MutableObservableState
import com.example.expensetrack.ui.domain.ObservableState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull


suspend fun <T> Flow<T>.collectOnce(action: suspend (value: T) -> Unit) {
    firstOrNull()?.let {
        action(it)
    }
}

fun createErrorState() = MutableObservableState(
    initial = null,
    update = { value: String?, _: String? -> value },
    output = { it },
)

@Composable
fun <Input, Output> ObservableState<Input, Output>.collectAsState(): State<Output> {
    return collectAsState(initial = output(value))
}