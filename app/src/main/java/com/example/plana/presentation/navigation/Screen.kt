package com.example.plana.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Transactions : Screen("transactions")
    data object AddTransaction : Screen("add-transaction")
    data object Budgets : Screen("budgets")
    data object Analytics : Screen("analytics")
    data object Calendar : Screen("calendar")
    data object Settings : Screen("settings")
}
