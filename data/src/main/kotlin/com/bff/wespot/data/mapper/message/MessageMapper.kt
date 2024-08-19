package com.bff.wespot.data.mapper.message

import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto

internal fun WrittenMessage.toWrittenMessageDto(): WrittenMessageDto = WrittenMessageDto(
    receiverId = receiverId,
    content = content,
    senderName = senderName,
    isAnonymous = isAnonymous,
)
