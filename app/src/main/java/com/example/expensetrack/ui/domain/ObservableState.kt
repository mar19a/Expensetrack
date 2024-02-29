package com.example.expensetrack.ui.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

open class ObservableState<Input, Output>(
    initial: Input,
    val output: (Input) -> Output,
) : Flow<Output> {

    protected val state = MutableStateFlow(initial)

    val value: Input
        get() = state.value

    override suspend fun collect(collector: FlowCollector<Output>) {
        state.map(output).collect(collector)
    }
}