package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.model.TransactionType
import com.example.plana.domain.repository.SettingsRepository
import com.example.plana.domain.repository.TransactionRepository
import com.example.plana.presentation.state.CalendarDay
import com.example.plana.presentation.state.CalendarHighlight
import com.example.plana.presentation.state.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale

class CalendarViewModel(
    private val transactionRepository: TransactionRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val zoneId = ZoneId.systemDefault()
    private val monthFlow = MutableStateFlow(YearMonth.now(zoneId))

    private val calendarData = monthFlow.flatMapLatest { month ->
        val start = month.atDay(1).atStartOfDay(zoneId).toInstant()
        val end = month.plusMonths(1).atDay(1).atStartOfDay(zoneId).toInstant()
        transactionRepository.observeBetween(start, end).map { items ->
            val expenseTotals = mutableMapOf<LocalDate, Double>()
            val incomeTotals = mutableMapOf<LocalDate, Double>()
            items.forEach { transaction ->
                val day = transaction.datetime.atZone(zoneId).toLocalDate()
                when (transaction.type) {
                    TransactionType.EXPENSE -> {
                        expenseTotals[day] = (expenseTotals[day] ?: 0.0) + transaction.amount
                    }
                    TransactionType.INCOME -> {
                        incomeTotals[day] = (incomeTotals[day] ?: 0.0) + transaction.amount
                    }
                }
            }

            CalendarData(
                month = month,
                days = buildCalendarDays(month, expenseTotals, incomeTotals),
                highlights = buildHighlights(expenseTotals, incomeTotals),
                monthExpenseTotal = expenseTotals.values.sum(),
                monthIncomeTotal = incomeTotals.values.sum()
            )
        }
    }

    val uiState = combine(
        calendarData,
        settingsRepository.currency,
        settingsRepository.hideAmounts
    ) { data, currency, hideAmounts ->
        CalendarUiState(
            month = data.month,
            days = data.days,
            highlights = data.highlights,
            monthExpenseTotal = data.monthExpenseTotal,
            monthIncomeTotal = data.monthIncomeTotal,
            currency = currency,
            hideAmounts = hideAmounts,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        CalendarUiState(
            month = YearMonth.now(zoneId),
            days = buildCalendarDays(YearMonth.now(zoneId), emptyMap(), emptyMap())
        )
    )

    fun goToPreviousMonth() {
        monthFlow.update { it.minusMonths(1) }
    }

    fun goToNextMonth() {
        monthFlow.update { it.plusMonths(1) }
    }
}

private data class CalendarData(
    val month: YearMonth,
    val days: List<CalendarDay>,
    val highlights: List<CalendarHighlight>,
    val monthExpenseTotal: Double,
    val monthIncomeTotal: Double
)

private fun buildCalendarDays(
    month: YearMonth,
    expenseTotals: Map<LocalDate, Double>,
    incomeTotals: Map<LocalDate, Double>
): List<CalendarDay> {
    val firstDayOfMonth = month.atDay(1)
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfWeek = weekFields.firstDayOfWeek
    val offset = (7 + firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value) % 7
    val gridStart = firstDayOfMonth.minusDays(offset.toLong())

    return (0 until 42).map { index ->
        val date = gridStart.plusDays(index.toLong())
        CalendarDay(
            date = date,
            isInCurrentMonth = date.month == month.month && date.year == month.year,
            expenseTotal = expenseTotals[date] ?: 0.0,
            incomeTotal = incomeTotals[date] ?: 0.0
        )
    }
}

private fun buildHighlights(
    expenseTotals: Map<LocalDate, Double>,
    incomeTotals: Map<LocalDate, Double>
): List<CalendarHighlight> {
    val days = (expenseTotals.keys + incomeTotals.keys).distinct()
    return days.map { day ->
        CalendarHighlight(
            date = day,
            expenseTotal = expenseTotals[day] ?: 0.0,
            incomeTotal = incomeTotals[day] ?: 0.0
        )
    }.sortedWith(
        compareByDescending<CalendarHighlight> { it.expenseTotal }
            .thenByDescending { it.incomeTotal }
    ).take(4)
}
