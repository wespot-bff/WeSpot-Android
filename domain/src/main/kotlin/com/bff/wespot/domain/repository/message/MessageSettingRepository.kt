package com.bff.wespot.domain.repository.message

import com.bff.wespot.model.message.response.Message

interface MessageSettingRepository {
    suspend fun getBlockedMessageList(): Result<List<Message>>

    suspend fun updateMessageBlockStatus(messageId: Int): Result<Unit>

    suspend fun updateMessageUsageStatus(enabled: Boolean): Result<Unit>
}
