package com.example.plana.data.repository

import com.example.plana.data.local.dao.CategoryDao
import com.example.plana.domain.model.Category
import com.example.plana.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun observeActive(): Flow<List<Category>> =
        categoryDao.observeActive().map { items -> items.map { it.toDomain() } }

    override fun observeAll(): Flow<List<Category>> =
        categoryDao.observeAll().map { items -> items.map { it.toDomain() } }

    override suspend fun upsert(category: Category): Long =
        categoryDao.upsert(category.toEntity())

    override suspend fun getById(id: Long): Category? =
        categoryDao.getById(id)?.toDomain()
}
