package com.bff.wespot.data.mapper.message

import com.bff.wespot.model.message.request.MessageType
import com.bff.wespot.model.message.request.SentMessage
import com.bff.wespot.data.remote.model.message.request.MessageTypeDto
import com.bff.wespot.data.remote.model.message.request.SentMessageDto

internal fun MessageType.toMessageTypeDto(): MessageTypeDto {
    return when(this) {
        MessageType.SENT -> MessageTypeDto.SENT
        MessageType.RECEIVED -> MessageTypeDto.RECEIVED
    }
}

internal fun SentMessage.toSentMessageDto(): SentMessageDto = SentMessageDto(
    receivedId = receivedId,
    schoolId = schoolId,
    grade = grade,
    group = group,
    content = content,
    sender = sender,
)
