package com.example.expensetrack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrack.data.repository.DataRepository
import com.example.expensetrack.data.repository.UserRepository
import com.example.expensetrack.di.Provider
import com.example.expensetrack.ui.screens.home.HomeState
import com.example.expensetrack.ui.screens.login.LoginState
import com.example.expensetrack.ui.screens.profile.ProfileState
import com.example.expensetrack.ui.screens.shared.filter_form.FilterFormState

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val loginState: LoginState = LoginState()
    val profileState: ProfileState
    val homeState: HomeState

    init {
        val provider = app as Provider
        val userRepository = provider.provider[UserRepository::class.java]!!
        val dataRepository = provider.provider[DataRepository::class.java]!!
        homeState = HomeState(
            repository = dataRepository,
            filterFormState = FilterFormState(dataRepository),
        )
        profileState = ProfileState(userRepository, viewModelScope)
    }
}
