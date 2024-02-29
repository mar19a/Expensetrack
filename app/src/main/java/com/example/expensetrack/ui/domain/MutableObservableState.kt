package com.example.expensetrack.ui.domain

class MutableObservableState<Input, Internal, Output>(
    initial: Internal,
    private val update: (Input, Internal) -> Internal,
    output: (Internal) -> Output,
) : ObservableState<Internal, Output>(initial, output) {
    fun emit(value: Input) {
        state.tryEmit(update(value, state.value))
    }
}