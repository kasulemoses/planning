package com.example.plana.domain.repository

import com.example.plana.domain.model.AggregateTotal
import com.example.plana.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observeBetween(start: Instant, end: Instant): Flow<List<Transaction>>
    fun observeTotalExpenses(start: Instant, end: Instant): Flow<Double>
    fun observeCategoryTotals(start: Instant, end: Instant): Flow<List<AggregateTotal>>
    fun observeAccountTotals(start: Instant, end: Instant): Flow<List<AggregateTotal>>
    suspend fun getById(id: Long): Transaction?
    suspend fun upsert(transaction: Transaction): Long
    suspend fun delete(transaction: Transaction)
}
