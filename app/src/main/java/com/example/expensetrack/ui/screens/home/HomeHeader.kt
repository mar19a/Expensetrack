package com.example.expensetrack.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetrack.R
import com.example.expensetrack.ui.screens.shared.filter_form.FilterForm
import com.example.expensetrack.ui.screens.shared.filter_form.FilterFormState
import com.example.expensetrack.ui.theme.Theme
import com.example.expensetrack.utils.collectAsState
import com.example.expensetrack.utils.currency


@Composable
fun HomeHeader(
    filterFormHidden: Boolean,
    reimbursed: Float,
    filterFormState: FilterFormState,
    changeFilterFormHidden: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        content = {
            ProvideTextStyle(
                value = MaterialTheme.typography.button,
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        content = {
                            Text(
                                text = stringResource(
                                    id = R.string.to_be_reimbursed,
                                    reimbursed.currency(),
                                )
                            )
                            Box(
                                content = {
                                    TextButton(
                                        onClick = {
                                            changeFilterFormHidden(!filterFormHidden)
                                        },
                                        content = {
                                            Text(
                                                text = stringResource(id = R.string.filters)
                                            )
                                            Icon(
                                                imageVector = Icons.Outlined.FilterList,
                                                contentDescription = stringResource(id = R.string.filters),
                                            )
                                        },
                                    )
                                    val from by filterFormState.from.collectAsState()
                                    val to by filterFormState.to.collectAsState()
                                    val max by filterFormState.max.collectAsState()
                                    val min by filterFormState.min.collectAsState()
                                    val category by filterFormState.category.collectAsState()
                                    val status by filterFormState.status.collectAsState()
                                    val count = listOf(from, to, max, min, category, status)
                                        .count { it.isNotBlank() }

                                    if (count > 0) Text(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .background(Theme.colors.error, CircleShape)
                                            .padding(2.dp),
                                        text = count.toString(),
                                        style = Theme.typography.overline,
                                        color = Theme.colors.onError,
                                    )
                                }
                            )
                        }
                    )
                }
            )
            Box(
                modifier = Modifier.animateContentSize(),
                content = {
                    if (!filterFormHidden) FilterForm(
                        state = filterFormState,
                        hideForm = {
                            changeFilterFormHidden(true)
                        }
                    )
                }
            )
        }
    )
} 
