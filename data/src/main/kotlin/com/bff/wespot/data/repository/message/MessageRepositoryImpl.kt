package com.bff.wespot.data.repository.message

import com.bff.wespot.data.mapper.message.toMessageTypeDto
import com.bff.wespot.data.mapper.message.toSentMessageDto
import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.result.Result
import com.bff.wespot.model.result.map
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageId
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.network.source.message.MessageDataSource
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): Result<MessageList, NetworkError> {
        return messageDataSource.getMessageList(messageType.toMessageTypeDto()).map(
            mapData = { messageListDto ->
                messageListDto.toMessageList()
            },
            mapError = { errorDto ->
                errorDto.toError()
            }
        )
    }

    override suspend fun postMessage(sentMessage: SentMessage): Result<MessageId, NetworkError> {
        return messageDataSource.postMessage(sentMessage.toSentMessageDto()).map(
            mapData = { messageIdDto ->
                messageIdDto.toMessageId()
            },
            mapError = { errorDto ->
                errorDto.toError()
            }
        )
    }

    override suspend fun getMessageStatus(): Result<MessageStatus, NetworkError> {
        return messageDataSource.getMessageStatus().map(
            mapData = { messageStatusDto ->
                messageStatusDto.toMessageStatus()
            },
            mapError = { errorDto ->
                errorDto.toError()
            }
        )
    }
}