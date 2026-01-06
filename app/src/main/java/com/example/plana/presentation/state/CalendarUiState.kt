package com.example.plana.presentation.state

import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val month: YearMonth = YearMonth.now(),
    val days: List<CalendarDay> = emptyList(),
    val highlights: List<CalendarHighlight> = emptyList(),
    val monthExpenseTotal: Double = 0.0,
    val monthIncomeTotal: Double = 0.0,
    val currency: String = "USD",
    val hideAmounts: Boolean = false,
    val isLoading: Boolean = true
)

data class CalendarDay(
    val date: LocalDate,
    val isInCurrentMonth: Boolean,
    val expenseTotal: Double,
    val incomeTotal: Double
)

data class CalendarHighlight(
    val date: LocalDate,
    val expenseTotal: Double,
    val incomeTotal: Double
)
