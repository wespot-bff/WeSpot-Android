package com.bff.wespot.domain.usecase.message

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.response.MessageStatus
import com.bff.wespot.model.result.NetworkError
import com.bff.wespot.model.result.Result
import javax.inject.Inject

class GetMessageStatusUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(): Result<MessageStatus, NetworkError> {
        return messageRepository.getMessageStatus()
    }
}
