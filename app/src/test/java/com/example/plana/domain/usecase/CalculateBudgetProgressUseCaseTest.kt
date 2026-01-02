package com.example.plana.domain.usecase

import com.example.plana.domain.model.Budget
import com.example.plana.domain.model.BudgetPeriod
import com.example.plana.domain.model.Transaction
import com.example.plana.domain.model.TransactionType
import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDate

class CalculateBudgetProgressUseCaseTest {
    @Test
    fun `calculates remaining budget for category`() = runTest {
        val now = Instant.now()
        val repo = FakeTransactionRepository(
            listOf(
                Transaction(
                    id = 1,
                    type = TransactionType.EXPENSE,
                    amount = 25.0,
                    datetime = now,
                    categoryId = 10,
                    accountId = null,
                    note = null,
                    paymentMethod = null,
                    createdAt = now,
                    updatedAt = now
                ),
                Transaction(
                    id = 2,
                    type = TransactionType.EXPENSE,
                    amount = 15.0,
                    datetime = now,
                    categoryId = 10,
                    accountId = null,
                    note = null,
                    paymentMethod = null,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )
        val budget = Budget(
            id = 1,
            categoryId = 10,
            limitAmount = 60.0,
            period = BudgetPeriod.MONTHLY,
            startDate = LocalDate.now()
        )

        val useCase = CalculateBudgetProgressUseCase(repo)
        val result = useCase(budget).first()

        assertEquals(40.0, result.remaining, 0.01)
    }
}

private class FakeTransactionRepository(
    items: List<Transaction>
) : TransactionRepository {
    private val flow = MutableStateFlow(items)

    override fun observeAll(): Flow<List<Transaction>> = flow

    override fun observeBetween(start: Instant, end: Instant): Flow<List<Transaction>> = flow

    override fun observeTotalExpenses(start: Instant, end: Instant): Flow<Double> =
        MutableStateFlow(flow.value.sumOf { it.amount })

    override fun observeCategoryTotals(start: Instant, end: Instant) =
        MutableStateFlow(emptyList<com.example.plana.domain.model.AggregateTotal>())

    override fun observeAccountTotals(start: Instant, end: Instant) =
        MutableStateFlow(emptyList<com.example.plana.domain.model.AggregateTotal>())

    override suspend fun getById(id: Long): Transaction? = flow.value.firstOrNull { it.id == id }

    override suspend fun upsert(transaction: Transaction): Long = transaction.id

    override suspend fun delete(transaction: Transaction) = Unit
}
