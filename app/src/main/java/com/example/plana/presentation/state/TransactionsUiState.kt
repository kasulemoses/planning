package com.example.plana.presentation.state

import com.example.plana.domain.model.Transaction


data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true
)
