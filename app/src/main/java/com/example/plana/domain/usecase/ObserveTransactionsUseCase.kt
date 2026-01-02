package com.example.plana.domain.usecase

import com.example.plana.domain.model.Transaction
import com.example.plana.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

class ObserveTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(start: Instant, end: Instant): Flow<List<Transaction>> {
        return transactionRepository.observeBetween(start, end)
    }
}
