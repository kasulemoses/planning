package com.example.plana.presentation.state

import com.example.plana.domain.model.AggregateTotal
import com.example.plana.domain.usecase.DailySummary


data class HomeUiState(
    val dailySummary: DailySummary? = null,
    val topCategories: List<AggregateTotal> = emptyList(),
    val currency: String = "USD",
    val hideAmounts: Boolean = false,
    val isLoading: Boolean = true
)
