package com.example.plana.domain.usecase

import com.example.plana.domain.model.Budget
import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId

class CalculateBudgetProgressUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(budget: Budget, zoneId: ZoneId = ZoneId.systemDefault()): Flow<BudgetProgress> {
        val start = budget.startDate.atStartOfDay(zoneId).toInstant()
        val end = when (budget.period) {
            com.example.plana.domain.model.BudgetPeriod.MONTHLY -> budget.startDate.plusMonths(1)
            com.example.plana.domain.model.BudgetPeriod.WEEKLY -> budget.startDate.plusWeeks(1)
        }.atStartOfDay(zoneId).toInstant()

        return transactionRepository.observeBetween(start, end).map { items ->
            val filtered = if (budget.categoryId != null) {
                items.filter { it.categoryId == budget.categoryId }
            } else {
                items
            }
            val spent = filtered.sumOf { it.amount }
            BudgetProgress(
                spent = spent,
                limit = budget.limitAmount,
                remaining = budget.limitAmount - spent
            )
        }
    }
}

data class BudgetProgress(
    val spent: Double,
    val limit: Double,
    val remaining: Double
)
