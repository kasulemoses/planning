package com.example.plana.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "transaction_tags",
    primaryKeys = ["transactionId", "tagId"]
)
data class TransactionTagCrossRef(
    val transactionId: Long,
    val tagId: Long
)
