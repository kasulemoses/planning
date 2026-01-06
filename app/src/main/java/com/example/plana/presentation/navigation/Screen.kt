package com.example.plana.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Transactions : Screen("transactions")
    data object AddTransaction : Screen("add-transaction?transactionId={transactionId}") {
        fun createRoute(transactionId: Long? = null): String {
            return if (transactionId == null) {
                "add-transaction"
            } else {
                "add-transaction?transactionId=$transactionId"
            }
        }
    }
    data object Budgets : Screen("budgets")
    data object Analytics : Screen("analytics")
    data object Calendar : Screen("calendar")
    data object Settings : Screen("settings")
}
