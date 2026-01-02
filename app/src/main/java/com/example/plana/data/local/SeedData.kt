package com.example.plana.data.local

import com.example.plana.data.local.entities.AccountEntity
import com.example.plana.data.local.entities.CategoryEntity
import com.example.plana.data.local.entities.CategoryType

object SeedData {
    val defaultCategories = listOf(
        CategoryEntity(name = "Food", iconKey = "restaurant", color = 0xFFE57373, type = CategoryType.EXPENSE),
        CategoryEntity(name = "Transport", iconKey = "directions_car", color = 0xFF64B5F6, type = CategoryType.EXPENSE),
        CategoryEntity(name = "Health", iconKey = "health_and_safety", color = 0xFF81C784, type = CategoryType.EXPENSE),
        CategoryEntity(name = "Salary", iconKey = "payments", color = 0xFFBA68C8, type = CategoryType.INCOME)
    )

    val defaultAccounts = listOf(
        AccountEntity(name = "Cash", type = "Wallet", color = 0xFFFFCC80, openingBalance = 0.0),
        AccountEntity(name = "Bank", type = "Bank", color = 0xFF90CAF9, openingBalance = 0.0)
    )
}
