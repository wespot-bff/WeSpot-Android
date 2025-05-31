package com.bff.wespot.domain.repository.message

import androidx.paging.PagingData
import com.bff.wespot.model.message.response.ReceivedMessage
import kotlinx.coroutines.flow.Flow

interface MessageStorageRepository {
    fun fetchReceivedMessageStream(): Flow<PagingData<ReceivedMessage>>

    suspend fun updateMessageReadStatus(messageId: Int): Result<Unit>

    suspend fun deleteMessage(messageId: Int): Result<Unit>

    suspend fun blockMessage(messageId: Int): Result<Unit>

    suspend fun unBlockMessage(messageId: Int): Result<Unit>
}
