package com.example.plana.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plana.data.local.PlanADatabase
import com.example.plana.data.local.SeedData
import com.example.plana.data.local.dao.AccountDao
import com.example.plana.data.local.dao.AttachmentDao
import com.example.plana.data.local.dao.BudgetDao
import com.example.plana.data.local.dao.CategoryDao
import com.example.plana.data.local.dao.RecurringRuleDao
import com.example.plana.data.local.dao.TagDao
import com.example.plana.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlanADatabase {
        return Room.databaseBuilder(context, PlanADatabase::class.java, "plana.db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    if (com.example.plana.BuildConfig.DEBUG) {
                        SeedData.defaultCategories.forEach { category ->
                            db.execSQL(
                                \"INSERT INTO categories (name, iconKey, color, type, isArchived, spendingLimit) VALUES (?, ?, ?, ?, ?, ?)\",
                                arrayOf(category.name, category.iconKey, category.color, category.type.name, category.isArchived, category.spendingLimit)
                            )
                        }
                        SeedData.defaultAccounts.forEach { account ->
                            db.execSQL(
                                \"INSERT INTO accounts (name, type, color, isArchived, openingBalance) VALUES (?, ?, ?, ?, ?)\",
                                arrayOf(account.name, account.type, account.color, account.isArchived, account.openingBalance)
                            )
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideTransactionDao(db: PlanADatabase): TransactionDao = db.transactionDao()
    @Provides fun provideCategoryDao(db: PlanADatabase): CategoryDao = db.categoryDao()
    @Provides fun provideAccountDao(db: PlanADatabase): AccountDao = db.accountDao()
    @Provides fun provideBudgetDao(db: PlanADatabase): BudgetDao = db.budgetDao()
    @Provides fun provideTagDao(db: PlanADatabase): TagDao = db.tagDao()
    @Provides fun provideAttachmentDao(db: PlanADatabase): AttachmentDao = db.attachmentDao()
    @Provides fun provideRecurringRuleDao(db: PlanADatabase): RecurringRuleDao = db.recurringRuleDao()
}
