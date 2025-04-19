package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDetailDto
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto

interface MessageDataSource {
    suspend fun postMessage(writtenMessageDto: WrittenMessageDto): Result<MessageIdDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto>

    suspend fun editMessage(messageId: Int, writtenMessageDto: WrittenMessageDto): Result<Unit>

    suspend fun getMessage(messageId: Int): Result<MessageDetailDto>

    suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto>
}
