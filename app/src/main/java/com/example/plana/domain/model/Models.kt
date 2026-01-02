package com.example.plana.domain.model

import java.time.Instant
import java.time.LocalDate


data class Transaction(
    val id: Long,
    val type: TransactionType,
    val amount: Double,
    val datetime: Instant,
    val categoryId: Long?,
    val accountId: Long?,
    val note: String?,
    val paymentMethod: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class TransactionType {
    EXPENSE,
    INCOME
}

data class Category(
    val id: Long,
    val name: String,
    val iconKey: String,
    val color: Long,
    val type: CategoryType,
    val isArchived: Boolean,
    val spendingLimit: Double?
)

enum class CategoryType {
    EXPENSE,
    INCOME,
    BOTH
}

data class Account(
    val id: Long,
    val name: String,
    val type: String,
    val color: Long,
    val isArchived: Boolean,
    val openingBalance: Double
)

data class Budget(
    val id: Long,
    val categoryId: Long?,
    val limitAmount: Double,
    val period: BudgetPeriod,
    val startDate: LocalDate
)

enum class BudgetPeriod {
    MONTHLY,
    WEEKLY
}

data class Tag(
    val id: Long,
    val name: String
)

data class Attachment(
    val id: Long,
    val transactionId: Long,
    val uriString: String,
    val mimeType: String
)

data class RecurringRule(
    val id: Long,
    val templateAmount: Double,
    val templateNote: String?,
    val templateCategoryId: Long?,
    val templateAccountId: Long?,
    val templatePaymentMethod: String?,
    val templateType: TransactionType,
    val frequency: RecurringFrequency,
    val interval: Int,
    val nextRunAt: Instant,
    val endAt: Instant?,
    val isActive: Boolean
)

enum class RecurringFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM
}

data class AggregateTotal(
    val id: Long?,
    val total: Double
)
