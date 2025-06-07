package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.response.MessageDto

interface MessageStorageDataSource {
    suspend fun getMessageList(): Result<List<MessageDto>>

    suspend fun getBookmarkedMessageList(): Result<List<MessageDto>>

    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun updateMessageBookmarkStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>
}
