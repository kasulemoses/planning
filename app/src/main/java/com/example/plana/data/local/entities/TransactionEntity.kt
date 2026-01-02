package com.example.plana.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("datetime"),
        Index("categoryId"),
        Index("accountId")
    ]
)

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType = TransactionType.EXPENSE,
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
