package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.response.Message
import com.bff.wespot.model.message.response.MessageRoom

interface MessageStorageRepository {
    suspend fun getMessages(): Result<List<Message>>

    suspend fun getBookmarkedMessages(): Result<List<Message>>

    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun updateMessageBookmarkStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>

    suspend fun getMessageRoom(roomId: Int): Result<MessageRoom>
}
