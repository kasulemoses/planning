package com.example.plana.data.repository

import com.example.plana.data.local.dao.BudgetDao
import com.example.plana.domain.model.Budget
import com.example.plana.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    override fun observeAll(): Flow<List<Budget>> =
        budgetDao.observeAll().map { items -> items.map { it.toDomain() } }

    override suspend fun getActiveForDate(date: LocalDate): List<Budget> =
        budgetDao.getActiveForDate(date).map { it.toDomain() }

    override suspend fun upsert(budget: Budget): Long =
        budgetDao.upsert(budget.toEntity())
}
