package com.bff.wespot.domain.usecase.message

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.MessageList
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.model.result.Result
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(messageType: MessageType): Result<MessageList, NetworkError> {
        return messageRepository.getMessageList(messageType)
    }
}
