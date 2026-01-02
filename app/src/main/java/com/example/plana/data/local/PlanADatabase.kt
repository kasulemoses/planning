package com.example.plana.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.plana.data.local.dao.AccountDao
import com.example.plana.data.local.dao.AttachmentDao
import com.example.plana.data.local.dao.BudgetDao
import com.example.plana.data.local.dao.CategoryDao
import com.example.plana.data.local.dao.RecurringRuleDao
import com.example.plana.data.local.dao.TagDao
import com.example.plana.data.local.dao.TransactionDao
import com.example.plana.data.local.entities.AccountEntity
import com.example.plana.data.local.entities.AttachmentEntity
import com.example.plana.data.local.entities.BudgetEntity
import com.example.plana.data.local.entities.CategoryEntity
import com.example.plana.data.local.entities.RecurringRuleEntity
import com.example.plana.data.local.entities.TagEntity
import com.example.plana.data.local.entities.TransactionEntity
import com.example.plana.data.local.entities.TransactionTagCrossRef

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        TagEntity::class,
        TransactionTagCrossRef::class,
        AttachmentEntity::class,
        RecurringRuleEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PlanADatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun tagDao(): TagDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun recurringRuleDao(): RecurringRuleDao
}
