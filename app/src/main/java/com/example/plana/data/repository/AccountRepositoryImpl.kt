package com.example.plana.data.repository

import com.example.plana.data.local.dao.AccountDao
import com.example.plana.domain.model.Account
import com.example.plana.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {
    override fun observeActive(): Flow<List<Account>> =
        accountDao.observeActive().map { items -> items.map { it.toDomain() } }

    override fun observeAll(): Flow<List<Account>> =
        accountDao.observeAll().map { items -> items.map { it.toDomain() } }

    override suspend fun upsert(account: Account): Long =
        accountDao.upsert(account.toEntity())

    override suspend fun getById(id: Long): Account? =
        accountDao.getById(id)?.toDomain()
}
