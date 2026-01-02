package com.example.plana.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.plana.data.local.entities.RecurringRuleEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface RecurringRuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rule: RecurringRuleEntity): Long

    @Update
    suspend fun update(rule: RecurringRuleEntity)

    @Query("SELECT * FROM recurring_rules WHERE isActive = 1 ORDER BY nextRunAt")
    fun observeActive(): Flow<List<RecurringRuleEntity>>

    @Query("SELECT * FROM recurring_rules WHERE nextRunAt <= :instant AND isActive = 1")
    suspend fun getDueRules(instant: Instant): List<RecurringRuleEntity>
}
