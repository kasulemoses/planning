package com.example.plana.di

import com.example.plana.data.repository.AccountRepositoryImpl
import com.example.plana.data.repository.BudgetRepositoryImpl
import com.example.plana.data.repository.CategoryRepositoryImpl
import com.example.plana.data.repository.SettingsRepositoryImpl
import com.example.plana.data.repository.TransactionRepositoryImpl
import com.example.plana.domain.repository.AccountRepository
import com.example.plana.domain.repository.BudgetRepository
import com.example.plana.domain.repository.CategoryRepository
import com.example.plana.domain.repository.SettingsRepository
import com.example.plana.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
