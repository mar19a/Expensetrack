package com.example.expensetrack.ui.screens.shared

import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarViewMonth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.example.expensetrack.ui.screens.shared.input.Input
import com.example.expensetrack.ui.screens.shared.input.InputField
import com.example.expensetrack.ui.screens.shared.input.InputState
import com.example.expensetrack.utils.activity
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
fun DatePickerDialog(dismiss: () -> Unit, listener: (LocalDate) -> Unit) {
    val activity = LocalContext.current.activity ?: return
    val datePicker = MaterialDatePicker.Builder.datePicker().build()
    datePicker.show(activity.supportFragmentManager, datePicker.toString())
    datePicker.addOnPositiveButtonClickListener {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = it
        }
        val date = LocalDate.of(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] +1 ,
            calendar[Calendar.DAY_OF_MONTH]+1,
        )
        listener(date)
    }
    datePicker.addOnDismissListener {
        dismiss()
    }
}

@Composable
fun TimePickerDialog(dismiss: () -> Unit, listener: (LocalTime) -> Unit) {
    val activity = LocalContext.current.activity ?: return
    val timePicker = MaterialTimePicker.Builder().build()
    timePicker.show(activity.supportFragmentManager, timePicker.toString())
    timePicker.addOnPositiveButtonClickListener {
        listener(LocalTime.of(timePicker.hour, timePicker.minute))
    }
    timePicker.addOnDismissListener {
        dismiss()
    }
}

@Composable
fun DatePicker(
    title: String,
    state: InputState,
    onDateChange: (LocalDate) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker)
        DatePickerDialog(
            dismiss = {
                showDatePicker = false
            },
            listener = onDateChange
        )
    Input(
        title = title,
        state = state.copy(
            icon = Alignment.End to Icons.Outlined.CalendarViewMonth,
            readOnly = true,
        ),
        content = { interactions, inputState ->
            val isFocused by interactions.collectIsFocusedAsState()
            LaunchedEffect(
                key1 = isFocused,
                block = {
                    if (isFocused) showDatePicker = true
                }
            )
            InputField(
                state = inputState,
                interactionSource = interactions,
            )
        }
    )
}