package com.example.plana.domain.repository

import com.example.plana.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeActive(): Flow<List<Category>>
    fun observeAll(): Flow<List<Category>>
    suspend fun upsert(category: Category): Long
    suspend fun getById(id: Long): Category?
}
