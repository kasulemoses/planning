package com.example.plana.domain.usecase

import com.example.plana.domain.model.Transaction
import com.example.plana.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.delete(transaction)
    }
}
