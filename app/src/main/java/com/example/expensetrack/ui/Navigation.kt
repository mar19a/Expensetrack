package com.example.expensetrack.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.expensetrack.MainViewModel
import com.example.expensetrack.ui.screens.home.HomeScreen
import com.example.expensetrack.ui.screens.login.LoginScreen
import com.example.expensetrack.ui.screens.profile.ProfileScreen

sealed class Navigation(
    val route: String,
) {
    object Login : Navigation("/login")
    object Main : Navigation("/") {
        object Home : Navigation("/home")
        object Profile : Navigation("/profile")
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = Navigation.Login.route,
        modifier = modifier,
        builder = {
            loginGraph(viewModel = viewModel, navController = navController)
            mainGraph(viewModel = viewModel, navController = navController)
        }
    )
}

fun NavGraphBuilder.loginGraph(
    viewModel: MainViewModel,
    navController: NavController,
) {
    composable(
        route = Navigation.Login.route,
        content = {
            LoginScreen(
                state = viewModel.loginState,
                navigateToNextScreen = {
                    navController.navigate(
                        route = Navigation.Main.route,
                        builder = {
                            popUpTo(
                                route = Navigation.Login.route,
                                popUpToBuilder = {
                                    inclusive = true
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    viewModel: MainViewModel,
) {
    navigation(
        startDestination = Navigation.Main.Home.route,
        route = Navigation.Main.route,
        builder = {
            composable(
                route = Navigation.Main.Home.route,
                content = {
                    HomeScreen(
                        state = viewModel.homeState,
                        navigateToProfileScreen = {
                            navController.navigate(Navigation.Main.Profile.route)
                        },
                        logout = {
                            logout(navController)
                        }
                    )
                }
            )
            composable(
                route = Navigation.Main.Profile.route,
                content = {
                    ProfileScreen(
                        state = viewModel.profileState,
                        goBack = {
                            navController.popBackStack()
                        },
                        logout = {
                            logout(navController)
                        }
                    )
                }
            )
        }
    )
}

fun logout(navController: NavController) {
    navController.navigate(
        route = Navigation.Login.route,
        builder = {
            popUpTo(
                route = Navigation.Main.route,
                popUpToBuilder = {
                    inclusive = true
                }
            )
        }
    )
}
