package com.bff.wespot.domain.usecase.message

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.model.message.response.MessageId
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.model.result.Result
import javax.inject.Inject

class PostMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(sentMessage: SentMessage): Result<MessageId, NetworkError> {
        return messageRepository.postMessage(sentMessage)
    }
}
