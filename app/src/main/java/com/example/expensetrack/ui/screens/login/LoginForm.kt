package com.example.expensetrack.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.expensetrack.R
import com.example.expensetrack.ui.screens.shared.input.Input
import com.example.expensetrack.ui.screens.shared.input.InputState
import com.example.expensetrack.ui.theme.secondaryPadding
import com.example.expensetrack.utils.collectAsState

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    state: LoginState,
) = with(state) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            secondaryPadding(), Alignment.CenterVertically
        ),
        content = {
            Input(
                title = stringResource(id = R.string.username),
                state = InputState(
                    value = username.collectAsState().value,
                    color = Color.White,
                    onValueChanged = username::emit
                ),
            )
            Input(
                title = stringResource(id = R.string.password),
                state = InputState(
                    value = password.collectAsState().value,
                    color = Color.White,
                    onValueChanged = password::emit,
                    visualTransformation = PasswordVisualTransformation(),
                    icon = Alignment.End to Icons.Outlined.RemoveRedEye,
                )
            )
        }
    )
}
