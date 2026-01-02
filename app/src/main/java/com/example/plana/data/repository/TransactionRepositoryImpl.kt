package com.example.plana.data.repository

import com.example.plana.data.local.dao.TransactionDao
import com.example.plana.domain.model.AggregateTotal
import com.example.plana.domain.model.Transaction
import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun observeAll(): Flow<List<Transaction>> =
        transactionDao.observeAll().map { items -> items.map { it.toDomain() } }

    override fun observeBetween(start: Instant, end: Instant): Flow<List<Transaction>> =
        transactionDao.observeBetween(start, end).map { items -> items.map { it.toDomain() } }

    override fun observeTotalExpenses(start: Instant, end: Instant): Flow<Double> =
        transactionDao.observeTotalExpenses(start, end).map { it ?: 0.0 }

    override fun observeCategoryTotals(start: Instant, end: Instant): Flow<List<AggregateTotal>> =
        transactionDao.observeCategoryTotals(start, end).map { items -> items.map { it.toDomain() } }

    override fun observeAccountTotals(start: Instant, end: Instant): Flow<List<AggregateTotal>> =
        transactionDao.observeAccountTotals(start, end).map { items -> items.map { it.toDomain() } }

    override suspend fun getById(id: Long): Transaction? =
        transactionDao.getById(id)?.toDomain()

    override suspend fun upsert(transaction: Transaction): Long =
        transactionDao.upsert(transaction.toEntity())

    override suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }
}
