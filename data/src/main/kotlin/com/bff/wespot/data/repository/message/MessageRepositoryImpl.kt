package com.bff.wespot.data.repository.message

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.result.Result
import com.bff.wespot.model.result.map
import com.bff.wespot.model.message.MessageList
import com.bff.wespot.model.message.MessageType
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.network.source.message.MessageDataSource
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDataSource: MessageDataSource,
) : MessageRepository {
    override suspend fun getMessageList(
        messageType: MessageType,
    ): Result<MessageList, NetworkError> {
        return messageDataSource.getMessageList(messageType).map(
            mapData = { messageListDto ->
                messageListDto.toMessageList()
            },
            mapError = { errorDto ->
                errorDto.toError()
            }
        )
    }
}