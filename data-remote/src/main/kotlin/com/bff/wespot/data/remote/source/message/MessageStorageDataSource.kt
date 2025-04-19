package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.response.MessageListDto

interface MessageStorageDataSource {
    suspend fun getMessageList(lastCursorId: Int?): Result<MessageListDto>

    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun updateMessageBookmarkStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>

    suspend fun blockMessage(messageId: Int): Result<Unit>

    suspend fun unBlockMessage(messageId: Int): Result<Unit>
}
