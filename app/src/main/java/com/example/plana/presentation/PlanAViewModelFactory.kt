package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.plana.di.AppContainer

class PlanAViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    observeDailySummaryUseCase = appContainer.observeDailySummaryUseCase,
                    transactionRepository = appContainer.transactionRepository,
                    settingsRepository = appContainer.settingsRepository
                )
            }
            modelClass.isAssignableFrom(TransactionsViewModel::class.java) -> {
                TransactionsViewModel(
                    transactionRepository = appContainer.transactionRepository
                )
            }
            modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java) -> {
                AddEditTransactionViewModel(
                    addTransactionUseCase = appContainer.addTransactionUseCase,
                    categoryRepository = appContainer.categoryRepository,
                    settingsRepository = appContainer.settingsRepository
                )
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(settingsRepository = appContainer.settingsRepository)
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }
}
