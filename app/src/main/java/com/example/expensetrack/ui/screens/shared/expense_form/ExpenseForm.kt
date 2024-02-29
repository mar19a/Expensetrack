package com.example.expensetrack.ui.screens.shared.expense_form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetrack.R
import com.example.expensetrack.ui.screens.shared.*
import com.example.expensetrack.ui.screens.shared.input.DefaultInputColor
import com.example.expensetrack.ui.screens.shared.input.Input
import com.example.expensetrack.ui.screens.shared.input.InputField
import com.example.expensetrack.ui.screens.shared.input.InputState
import com.example.expensetrack.ui.theme.ErrorText
import com.example.expensetrack.ui.theme.Theme
import com.example.expensetrack.ui.theme.secondaryPadding
import com.example.expensetrack.utils.collectAsState


@Composable
fun ExpenseForm(
    state: ExpenseFormState,
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit = {},
) = with(state) {
    if (showGallery.collectAsState().value) {
        ImageSelect(
            onImageSelected = {
                receipt.emit(it)
                showGallery.emit(false)
            }
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            secondaryPadding(),
            Alignment.CenterVertically
        ),
        content = {
            Input(
                title = stringResource(id = R.string.category),
                state = InputState(
                    value = category.collectAsState().value,
                    readOnly = true,
                    required = true,
                    error = categoryError.collectAsState().value,
                ),
                content = { interactionSource, state ->
                    DropdownField(
                        suggested = state.value,
                        suggestions = stringArrayResource(id = R.array.Category).toList(),
                        onSuggestionSelected = category::emit,
                        dropdownField = { _, expanded ->
                            InputField(
                                state = state.copy(
                                    icon = Alignment.End to if (expanded) {
                                        Icons.Outlined.ArrowDropUp
                                    } else Icons.Outlined.ArrowDropDown
                                ),
                                interactionSource = interactionSource,
                            )
                        }
                    )
                }
            )
            Input(
                title = stringResource(id = R.string.total),
                state = InputState(
                    value = total.collectAsState().value,
                    onValueChanged = total::emit,
                    icon = Alignment.Start to Icons.Outlined.AttachMoney,
                    error = totalError.collectAsState().value,
                    required = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                )
            )
            DatePicker(
                title = stringResource(id = R.string.date),
                state = InputState(
                    value = date.collectAsState().value,
                    error = dateError.collectAsState().value,
                    required = true,
                ),
                onDateChange = date::emit,
            )
            Input(
                title = stringResource(id = R.string.comment),
                state = InputState(
                    value = comment.collectAsState().value,
                    onValueChanged = comment::emit
                ),
                content = { interactionSource, state ->
                    InputField(
                        modifier = Modifier.height(56.dp * 4),
                        state = state,
                        interactionSource = interactionSource,
                    )
                }
            )
            Input(
                title = stringResource(id = R.string.status),
                state = InputState(status.collectAsState().value ?: ""),
                content = { interactionSource, state ->
                    val statuses = stringArrayResource(id = R.array.Status).toList()
                    Column(
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                interactionSource.tryEmit(HoverInteraction.Enter())
                            }
                        ),
                        content = {
                            statuses.chunked(2).forEach { row ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        secondaryPadding() / 2
                                    ),
                                    content = {
                                        row.forEach { item ->
                                            CheckboxItem(
                                                checked = state.value == item,
                                                onCheckedChange = { isChecked ->
                                                    status.emit(isChecked to item)
                                                },
                                                text = {
                                                    Text(item)
                                                }
                                            )
                                        }
                                    }
                                )
                            }
                            statusError.collectAsState().value?.let {
                                ErrorText(error = it)
                            }
                        }
                    )
                }
            )
            TextButton(
                content = {
                    Text(stringResource(id = R.string.select_receipt))
                },
                onClick = {
                    showGallery.emit(true)
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = DefaultInputColor.copy(0.05f)
                ),
            )
            Avatar(
                model = receipt.collectAsState().value,
                shape = RoundedCornerShape(secondaryPadding()),
                size = 1f,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(secondaryPadding()),
                content = {
                    Button(
                        content = {
                            Text(stringResource(id = R.string.save))
                        },
                        onClick = onSaveClicked,
                    )
                    TextButton(
                        content = {
                            Text(stringResource(id = R.string.cancel))
                        },
                        onClick = onCancelClicked,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = DefaultInputColor.copy(0.05f)
                        ),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (!state.isEdit) TextButton(
                        content = {
                            Text(stringResource(id = R.string.delete))
                        },
                        onClick = onDeleteClicked,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = DefaultInputColor.copy(0.05f),
                            contentColor = Theme.colors.error,
                        ),
                    )
                }
            )
        }
    )
}