package com.bff.wespot.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bff.wespot.data.local.model.message.ReceivedMessageEntity

@Dao
interface ReceivedMessageDao {
    @Upsert
    suspend fun upsertAll(users: List<ReceivedMessageEntity>)

    @Query("SELECT * FROM received_message")
    fun pagingSource(): PagingSource<Int, ReceivedMessageEntity>

    @Query("DELETE FROM received_message")
    suspend fun clearReceivedMessage()

    @Query("DELETE FROM received_message WHERE id = :messageId")
    suspend fun deleteReceivedMessage(messageId: Int)

    @Query("UPDATE received_message SET is_read = 1 WHERE id = :messageId")
    suspend fun updateReadStatus(messageId: Int)
}
