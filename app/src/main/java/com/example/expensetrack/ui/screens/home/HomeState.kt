package com.example.expensetrack.ui.screens.home

import com.example.expensetrack.data.repository.DataRepository
import com.example.expensetrack.ui.domain.MutableObservableState
import com.example.expensetrack.ui.screens.shared.expense_form.ExpenseFormState
import com.example.expensetrack.ui.screens.shared.filter_form.FilterFormState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeState(
    val repository: DataRepository,
    val filterFormState: FilterFormState,
) {
    val reimbursed: Flow<Float>
        get() = repository.data.map { list ->
            list.filter {
                it.status.equals("reimbursed", ignoreCase = true)
            }.map { it.total }.reduceOrNull { acc, current -> acc + current } ?: 0f
        }

    val expense = MutableObservableState(
        initial = ExpenseFormState(),
        update = { state: ExpenseFormState, _ -> state },
        output = { it }
    )

    suspend fun save(): Boolean {
        val newExpense = expense.value.save() ?: return false
        expense.value.clear()
        repository.add(newExpense)
        return true
    }
}