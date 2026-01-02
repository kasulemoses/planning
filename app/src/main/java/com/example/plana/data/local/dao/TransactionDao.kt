package com.example.plana.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.plana.data.local.entities.TransactionEntity
import com.example.plana.data.local.relations.TransactionWithTags
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions ORDER BY datetime DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT * FROM transactions
        WHERE datetime BETWEEN :start AND :end
        ORDER BY datetime DESC
        """
    )
    fun observeBetween(start: Instant, end: Instant): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT SUM(amount) FROM transactions
        WHERE datetime BETWEEN :start AND :end
        AND type = 'EXPENSE'
        """
    )
    fun observeTotalExpenses(start: Instant, end: Instant): Flow<Double?>

    @Query(
        """
        SELECT categoryId as id, SUM(amount) as total
        FROM transactions
        WHERE datetime BETWEEN :start AND :end
        AND type = 'EXPENSE'
        GROUP BY categoryId
        ORDER BY total DESC
        """
    )
    fun observeCategoryTotals(start: Instant, end: Instant): Flow<List<CategoryTotal>>

    @Query(
        """
        SELECT accountId as id, SUM(amount) as total
        FROM transactions
        WHERE datetime BETWEEN :start AND :end
        GROUP BY accountId
        ORDER BY total DESC
        """
    )
    fun observeAccountTotals(start: Instant, end: Instant): Flow<List<AccountTotal>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getWithTags(transactionId: Long): TransactionWithTags?
}

data class CategoryTotal(
    val id: Long?,
    val total: Double
)

data class AccountTotal(
    val id: Long?,
    val total: Double
)
