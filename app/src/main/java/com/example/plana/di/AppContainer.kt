package com.example.plana.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plana.data.local.PlanADatabase
import com.example.plana.data.local.SeedData
import com.example.plana.data.preferences.SettingsDataStore
import com.example.plana.data.repository.AccountRepositoryImpl
import com.example.plana.data.repository.BudgetRepositoryImpl
import com.example.plana.data.repository.CategoryRepositoryImpl
import com.example.plana.data.repository.SettingsRepositoryImpl
import com.example.plana.data.repository.TransactionRepositoryImpl
import com.example.plana.domain.usecase.AddTransactionUseCase
import com.example.plana.domain.usecase.CalculateBudgetProgressUseCase
import com.example.plana.domain.usecase.DeleteTransactionUseCase
import com.example.plana.domain.usecase.ObserveDailySummaryUseCase
import com.example.plana.domain.usecase.ObserveTransactionsUseCase

class AppContainer(context: Context) {
    private val database: PlanADatabase = Room.databaseBuilder(
        context.applicationContext,
        PlanADatabase::class.java,
        "plana.db"
    )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onCreate(db)
                if (com.example.plana.BuildConfig.DEBUG) {
                    SeedData.defaultCategories.forEach { category ->
                        db.execSQL(
                            "INSERT INTO categories (name, iconKey, color, type, isArchived, spendingLimit) VALUES (?, ?, ?, ?, ?, ?)",
                            arrayOf(
                                category.name,
                                category.iconKey,
                                category.color,
                                category.type.name,
                                category.isArchived,
                                category.spendingLimit
                            )
                        )
                    }
                    SeedData.defaultAccounts.forEach { account ->
                        db.execSQL(
                            "INSERT INTO accounts (name, type, color, isArchived, openingBalance) VALUES (?, ?, ?, ?, ?)",
                            arrayOf(
                                account.name,
                                account.type,
                                account.color,
                                account.isArchived,
                                account.openingBalance
                            )
                        )
                    }
                }
            }
        })
        .fallbackToDestructiveMigration()
        .build()

    private val settingsDataStore = SettingsDataStore(context.applicationContext)

    val transactionRepository = TransactionRepositoryImpl(database.transactionDao())
    val categoryRepository = CategoryRepositoryImpl(database.categoryDao())
    val accountRepository = AccountRepositoryImpl(database.accountDao())
    val budgetRepository = BudgetRepositoryImpl(database.budgetDao())
    val settingsRepository = SettingsRepositoryImpl(settingsDataStore)

    val addTransactionUseCase = AddTransactionUseCase(transactionRepository)
    val deleteTransactionUseCase = DeleteTransactionUseCase(transactionRepository)
    val observeTransactionsUseCase = ObserveTransactionsUseCase(transactionRepository)
    val observeDailySummaryUseCase = ObserveDailySummaryUseCase(transactionRepository)
    val calculateBudgetProgressUseCase = CalculateBudgetProgressUseCase(
        transactionRepository = transactionRepository
    )
}
