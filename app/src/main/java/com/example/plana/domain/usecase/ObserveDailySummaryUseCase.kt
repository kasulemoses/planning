package com.example.plana.domain.usecase

import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class ObserveDailySummaryUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(zoneId: ZoneId = ZoneId.systemDefault()): Flow<DailySummary> {
        val today = LocalDate.now(zoneId)
        val yesterday = today.minusDays(1)
        val todayRange = today.atStartOfDay(zoneId).toInstant() to today.plusDays(1)
            .atStartOfDay(zoneId).toInstant()
        val yesterdayRange = yesterday.atStartOfDay(zoneId).toInstant() to yesterday.plusDays(1)
            .atStartOfDay(zoneId).toInstant()
        val weekStart = today.minusDays(6).atStartOfDay(zoneId).toInstant()
        val weekEnd = today.plusDays(1).atStartOfDay(zoneId).toInstant()

        val todayTotal = transactionRepository.observeTotalExpenses(todayRange.first, todayRange.second)
        val yesterdayTotal = transactionRepository.observeTotalExpenses(yesterdayRange.first, yesterdayRange.second)
        val weekTransactions = transactionRepository.observeBetween(weekStart, weekEnd)

        return combine(todayTotal, yesterdayTotal, weekTransactions) { todayAmount, yesterdayAmount, weekItems ->
            DailySummary(
                todayTotal = todayAmount,
                yesterdayTotal = yesterdayAmount,
                last7Days = summarizeByDay(weekItems, zoneId, weekStart, weekEnd)
            )
        }
    }

    private fun summarizeByDay(
        items: List<com.example.plana.domain.model.Transaction>,
        zoneId: ZoneId,
        start: Instant,
        end: Instant
    ): List<DailyTotal> {
        val dayMap = items.groupBy { it.datetime.atZone(zoneId).toLocalDate() }
        val days = generateSequence(start.atZone(zoneId).toLocalDate()) { it.plusDays(1) }
            .takeWhile { it.isBefore(end.atZone(zoneId).toLocalDate()) }
            .toList()

        return days.map { day ->
            val total = dayMap[day].orEmpty().sumOf { it.amount }
            DailyTotal(day = day, total = total)
        }
    }
}

data class DailySummary(
    val todayTotal: Double,
    val yesterdayTotal: Double,
    val last7Days: List<DailyTotal>
)

data class DailyTotal(
    val day: LocalDate,
    val total: Double
)
