package com.bff.wespot.domain.repository.message

interface MessageStorageRepository {
    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>

    suspend fun blockMessage(messageId: Int): Result<Unit>

    suspend fun reportMessage(messageId: Int): Result<Unit>
}
