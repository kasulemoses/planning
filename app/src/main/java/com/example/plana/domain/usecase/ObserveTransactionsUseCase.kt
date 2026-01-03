package com.example.plana.domain.usecase

import com.example.plana.domain.model.Transaction
import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant

class ObserveTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(start: Instant, end: Instant): Flow<List<Transaction>> {
        return transactionRepository.observeBetween(start, end)
    }
}
