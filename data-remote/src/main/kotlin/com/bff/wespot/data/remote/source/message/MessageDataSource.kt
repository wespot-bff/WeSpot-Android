package com.bff.wespot.data.remote.source.message

import com.bff.wespot.data.remote.model.message.request.SendMessageDto
import com.bff.wespot.data.remote.model.message.response.BlockedMessageListDto
import com.bff.wespot.data.remote.model.message.response.MessageDetailDto
import com.bff.wespot.data.remote.model.message.response.MessageHomeTitleDto
import com.bff.wespot.data.remote.model.message.response.MessageStatusDto
import com.bff.wespot.data.remote.model.message.response.SenderProfileDto

interface MessageDataSource {
    suspend fun getSenderProfileList(receiverId: Int): Result<List<SenderProfileDto>>

    suspend fun postMessage(sendMessage: SendMessageDto): Result<Unit>

    suspend fun getMessageStatus(): Result<MessageStatusDto>

    suspend fun getMessage(messageId: Int): Result<MessageDetailDto>

    suspend fun getBlockedMessage(cursorId: Int?): Result<BlockedMessageListDto>

    suspend fun getMessageHomeTitle(): Result<MessageHomeTitleDto>
}
