package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.repository.SettingsRepository
import com.example.plana.domain.repository.TransactionRepository
import com.example.plana.domain.usecase.ObserveDailySummaryUseCase
import com.example.plana.presentation.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeDailySummaryUseCase: ObserveDailySummaryUseCase,
    transactionRepository: TransactionRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val zoneId = ZoneId.systemDefault()
    private val today = LocalDate.now(zoneId)
    private val start = today.atStartOfDay(zoneId).toInstant()
    private val end = today.plusDays(1).atStartOfDay(zoneId).toInstant()

    val uiState = combine(
        observeDailySummaryUseCase(zoneId),
        transactionRepository.observeCategoryTotals(start, end),
        settingsRepository.currency,
        settingsRepository.hideAmounts
    ) { summary, categories, currency, hideAmounts ->
        HomeUiState(
            dailySummary = summary,
            topCategories = categories.take(5),
            currency = currency,
            hideAmounts = hideAmounts,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
}
