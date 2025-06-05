package com.bff.wespot.data.local.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bff.wespot.data.local.model.message.MessageEntity

@Dao
interface MessageDao {
    @Upsert
    suspend fun upsertAll(users: List<MessageEntity>)

    @Query("SELECT * FROM message")
    fun getPagingSource(): PagingSource<Int, MessageEntity>

    @Query("SELECT * FROM message WHERE is_bookmarked = 1")
    fun getBookmarkedMessagePagingSource(): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM message")
    suspend fun clearMessages()

    @Query("DELETE FROM message WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

    @Query("UPDATE message SET is_exists_unread_message = 1 WHERE id = :messageId")
    suspend fun updateReadStatus(messageId: Int)

    @Query("UPDATE message SET is_bookmarked = NOT is_bookmarked WHERE id = :messageId")
    suspend fun updateBookmarkStatus(messageId: Int)
}
