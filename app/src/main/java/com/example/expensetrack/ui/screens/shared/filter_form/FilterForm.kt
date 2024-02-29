package com.example.expensetrack.ui.screens.shared.filter_form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import com.example.expensetrack.R
import com.example.expensetrack.ui.screens.shared.CheckboxItem
import com.example.expensetrack.ui.screens.shared.DatePicker
import com.example.expensetrack.ui.screens.shared.DropdownField
import com.example.expensetrack.ui.screens.shared.input.DefaultInputColor
import com.example.expensetrack.ui.screens.shared.input.Input
import com.example.expensetrack.ui.screens.shared.input.InputField
import com.example.expensetrack.ui.screens.shared.input.InputState
import com.example.expensetrack.ui.theme.Theme
import com.example.expensetrack.ui.theme.secondaryPadding
import com.example.expensetrack.utils.collectAsState
import kotlinx.coroutines.launch

@Composable
fun FilterForm(
    state: FilterFormState,
    modifier: Modifier = Modifier,
    hideForm: () -> Unit,
    sectionSpacing: Dp = secondaryPadding()
) = with(state) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(
            sectionSpacing,
            Alignment.CenterVertically
        ),
        content = {
            DatePicker(
                title = stringResource(id = R.string.from),
                state = InputState(from.collectAsState().value),
                onDateChange = from::emit,
            )
            DatePicker(
                title = stringResource(id = R.string.to),
                state = InputState(to.collectAsState().value),
                onDateChange = to::emit,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(secondaryPadding()),
                content = {
                    val textIcon = Alignment.Start to Icons.Outlined.AttachMoney
                    Input(
                        modifier = Modifier.weight(0.45f),
                        title = stringResource(id = R.string.min),
                        state = InputState(
                            value = min.collectAsState().value,
                            onValueChanged = min::emit,
                            icon = textIcon,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                        )
                    )
                    Input(
                        modifier = Modifier.weight(0.45f),
                        title = stringResource(id = R.string.max),
                        state = InputState(
                            value = max.collectAsState().value,
                            onValueChanged = max::emit,
                            icon = textIcon,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                        )
                    )
                }
            )
            Input(
                title = stringResource(id = R.string.category),
                state = InputState(
                    value = category.collectAsState().value,
                    readOnly = true,
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
                                    } else Icons.Outlined.ArrowDropDown,
                                ),
                                interactionSource = interactionSource,
                            )
                        }
                    )
                }
            )
            Input(
                title = stringResource(id = R.string.status),
                state = InputState(status.collectAsState().value),
                content = { interactionSource, state ->
                    val statuses = stringArrayResource(id = R.array.Status).toList()
                    statuses.chunked(2).forEach { row ->
                        Column(
                            modifier = Modifier.clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    interactionSource.tryEmit(HoverInteraction.Enter())
                                }
                            ),
                            content = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(secondaryPadding() / 2),
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
                        )
                    }
                }
            )
            val coroutineScope = rememberCoroutineScope()
            Row(
                horizontalArrangement = Arrangement.spacedBy(secondaryPadding()),
                content = {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        content = {
                            Text(stringResource(id = R.string.filter))
                        },
                        onClick = {
                            coroutineScope.launch {
                                save()
                                hideForm()
                            }
                        },
                    )
                    TextButton(
                        content = {
                            Text(stringResource(id = R.string.clear))
                        },
                        onClick = {
                            coroutineScope.launch {
                                clear()
                                save()
                                hideForm()
                            }
                        },
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
