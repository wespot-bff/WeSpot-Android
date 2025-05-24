package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.data.remote.model.message.response.MessageHomeTitleDto
import com.bff.wespot.data.remote.model.message.response.MessageIdDto
import com.bff.wespot.data.remote.model.message.response.SentMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto
import com.bff.wespot.data.remote.model.message.response.ReceivedMessageListDto

interface MessageDataSource {
    suspend fun getReceivedMessageList(cursorId: Int?): Result<ReceivedMessageListDto>

    suspend fun getSentMessageList(cursorId: Int?): Result<SentMessageListDto>

    suspend fun postMessage(writtenMessageDto: WrittenMessageDto): Result<MessageIdDto>

    suspend fun getMessageStatus(): Result<MessageStatusDto>

    suspend fun editMessage(messageId: Int, writtenMessageDto: WrittenMessageDto): Result<Unit>

    suspend fun getMessage(messageId: Int): Result<MessageDto>

    suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto>

    suspend fun getMessageHomeTitle(): Result<MessageHomeTitleDto>
}
