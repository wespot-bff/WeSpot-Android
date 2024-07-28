package com.bff.wespot.data.remote.source.message

interface MessageStorageDataSource {
    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>

    suspend fun blockMessage(messageId: Int): Result<Unit>

    suspend fun reportMessage(messageId: Int): Result<Unit>
}
