package com.example.plana.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.plana.data.local.entities.AttachmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attachment: AttachmentEntity): Long

    @Query("SELECT * FROM attachments WHERE transactionId = :transactionId")
    fun observeForTransaction(transactionId: Long): Flow<List<AttachmentEntity>>
}
