package com.example.plana.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.plana.data.local.entities.TransactionEntity
import com.example.plana.data.local.entities.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {
    private lateinit var database: PlanADatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlanADatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun totalExpensesBetween_returnsSum() = runTest {
        val now = Instant.now()
        database.transactionDao().upsert(
            TransactionEntity(
                amount = 12.0,
                datetime = now,
                categoryId = null,
                accountId = null,
                note = null,
                paymentMethod = null,
                createdAt = now,
                updatedAt = now
            )
        )
        database.transactionDao().upsert(
            TransactionEntity(
                amount = 8.0,
                datetime = now,
                categoryId = null,
                accountId = null,
                note = null,
                paymentMethod = null,
                createdAt = now,
                updatedAt = now,
                type = TransactionType.EXPENSE
            )
        )

        val total = database.transactionDao()
            .observeTotalExpenses(now.minusSeconds(60), now.plusSeconds(60))
            .first()

        assertEquals(20.0, total ?: 0.0, 0.01)
    }
}
