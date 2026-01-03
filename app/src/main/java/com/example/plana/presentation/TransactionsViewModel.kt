package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.repository.TransactionRepository
import com.example.plana.presentation.state.TransactionsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId

class TransactionsViewModel(
    transactionRepository: TransactionRepository
) : ViewModel() {
    private val zoneId = ZoneId.systemDefault()
    private val end = LocalDate.now(zoneId).plusDays(1).atStartOfDay(zoneId).toInstant()
    private val start = LocalDate.now(zoneId).minusDays(30).atStartOfDay(zoneId).toInstant()

    val uiState = transactionRepository.observeBetween(start, end)
        .map { items -> TransactionsUiState(transactions = items, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TransactionsUiState())
}
