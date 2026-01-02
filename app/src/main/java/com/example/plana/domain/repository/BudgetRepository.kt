package com.example.plana.domain.repository

import com.example.plana.domain.model.Budget
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface BudgetRepository {
    fun observeAll(): Flow<List<Budget>>
    suspend fun getActiveForDate(date: LocalDate): List<Budget>
    suspend fun upsert(budget: Budget): Long
}
