package com.example.expensetrack.ui.screens.login

import com.example.expensetrack.ui.domain.MutableObservableState

class LoginState {
    val username = MutableObservableState<String?, String?, String>(
        initial = null,
        update = { it, _ -> it },
        output = { it ?: "" }
    )

    val password = MutableObservableState<String?, String?, String>(
        initial = null,
        update = { it, _ -> it },
        output = { it ?: "" }
    )
}
