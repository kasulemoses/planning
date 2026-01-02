package com.example.plana.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "recurring_rules")
data class RecurringRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
    val isActive: Boolean = true
)

enum class RecurringFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    CUSTOM
}
