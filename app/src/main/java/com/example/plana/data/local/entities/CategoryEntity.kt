package com.example.plana.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconKey: String,
    val color: Long,
    val type: CategoryType = CategoryType.EXPENSE,
    val isArchived: Boolean = false,
    val spendingLimit: Double? = null
)

enum class CategoryType {
    EXPENSE,
    INCOME,
    BOTH
}
