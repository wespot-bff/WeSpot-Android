package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto

interface MessageDataSource {
    suspend fun getReceivedMessageList(cursorId: Int?): Result<MessageListDto>

    suspend fun getSentMessageList(cursorId: Int?): Result<MessageListDto>

    suspend fun postMessage(sentMessageDto: WrittenMessageDto): Result<MessageIdDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto>

    suspend fun editMessage(messageId: Int, sentMessageDto: WrittenMessageDto): Result<Unit>

    suspend fun getMessage(messageId: Int): Result<MessageDto>

    suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto>
}
