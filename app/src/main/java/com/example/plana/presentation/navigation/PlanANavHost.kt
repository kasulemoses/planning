package com.example.plana.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plana.di.AppContainer
import com.example.plana.presentation.AddEditTransactionViewModel
import com.example.plana.presentation.HomeViewModel
import com.example.plana.presentation.PlanAViewModelFactory
import com.example.plana.presentation.SettingsViewModel
import com.example.plana.presentation.TransactionsViewModel
import com.example.plana.presentation.screens.AddEditTransactionScreen
import com.example.plana.presentation.screens.AnalyticsScreen
import com.example.plana.presentation.screens.BudgetsScreen
import com.example.plana.presentation.screens.CalendarScreen
import com.example.plana.presentation.screens.HomeScreen
import com.example.plana.presentation.screens.SettingsScreen
import com.example.plana.presentation.screens.TransactionsScreen

@Composable
fun PlanANavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appContainer: AppContainer
) {
    val viewModelFactory = remember(appContainer) { PlanAViewModelFactory(appContainer) }
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            HomeScreen(
                uiState = viewModel.uiState,
                onAddTransaction = { navController.navigate(Screen.AddTransaction.route) },
                onViewTransactions = { navController.navigate(Screen.Transactions.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Transactions.route) {
            val viewModel: TransactionsViewModel = viewModel(factory = viewModelFactory)
            TransactionsScreen(
                uiState = viewModel.uiState,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AddTransaction.route) {
            val viewModel: AddEditTransactionViewModel = viewModel(factory = viewModelFactory)
            AddEditTransactionScreen(
                uiState = viewModel.uiState,
                onSave = { viewModel.save { navController.popBackStack() } },
                onBack = { navController.popBackStack() },
                onAmountChange = viewModel::updateAmount,
                onNoteChange = viewModel::updateNote,
                onCategoryChange = viewModel::updateCategory,
                onTypeChange = viewModel::updateType
            )
        }
        composable(Screen.Budgets.route) { BudgetsScreen() }
        composable(Screen.Analytics.route) { AnalyticsScreen() }
        composable(Screen.Calendar.route) { CalendarScreen() }
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = viewModel(factory = viewModelFactory)
            SettingsScreen(
                currency = viewModel.currency,
                hideAmounts = viewModel.hideAmounts,
                onToggleHide = viewModel::toggleHideAmounts,
                onCurrencyChange = viewModel::setCurrency,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
