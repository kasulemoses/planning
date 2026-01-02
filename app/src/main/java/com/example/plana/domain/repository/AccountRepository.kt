package com.example.plana.domain.repository

import com.example.plana.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun observeActive(): Flow<List<Account>>
    fun observeAll(): Flow<List<Account>>
    suspend fun upsert(account: Account): Long
    suspend fun getById(id: Long): Account?
}
