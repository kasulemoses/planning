package com.example.plana.domain.usecase

import com.example.plana.domain.model.Transaction
import com.example.plana.domain.repository.TransactionRepository

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Long {
        return transactionRepository.upsert(transaction)
    }
}
