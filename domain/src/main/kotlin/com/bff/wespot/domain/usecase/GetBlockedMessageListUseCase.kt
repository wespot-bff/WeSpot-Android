package com.bff.wespot.domain.usecase

import com.bff.wespot.domain.repository.message.MessageRepository
import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.response.Message
import javax.inject.Inject

class GetBlockedMessageListUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(cursorId: Int): Result<List<Message>> =
        messageRepository.getMessageList(MessageType.RECEIVED, cursorId).mapCatching {
            it.messages.filter { it.isBlocked }
        }
}
