package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.response.MessageDto

interface MessageSettingDataSource {
    suspend fun getBlockedMessageList(): Result<List<MessageDto>>

    suspend fun updateMessageBlockStatus(messageId: Int): Result<Unit>
}
