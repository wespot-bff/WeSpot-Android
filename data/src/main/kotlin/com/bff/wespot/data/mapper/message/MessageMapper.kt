package com.bff.wespot.data.mapper.message

import com.bff.wespot.data.local.model.message.ReceivedMessageEntity
import com.bff.wespot.data.local.model.message.ReceivedMessageEntityList
import com.bff.wespot.model.message.request.SendMessage
import com.bff.wespot.data.remote.model.message.request.SendMessageDto
import com.bff.wespot.data.remote.model.message.response.ReceivedMessageListDto

internal fun SendMessage.toDto(): SendMessageDto = SendMessageDto(
    receiverId = receiverId,
    content = content,
    isAnonymous = isAnonymous,
    imageUrl = imageUrl,
    name = name,
)

internal fun ReceivedMessageListDto.toEntity(
    lastCursorId: Int?,
) = ReceivedMessageEntityList(
    data = messages.map { it.toReceivedMessageEntity(lastCursorId) },
    lastCursorId = lastCursorId ?: -1,
    hasNext = hasNext,
)

private fun ReceivedMessageListDto.ReceivedMessageDto.toReceivedMessageEntity(
    lastCursorId: Int?,
): ReceivedMessageEntity = ReceivedMessageEntity(
    id = id,
    senderName = senderName,
    receiver = receiver.toUser(),
    content = content,
    sender = sender.toUser(),
    receivedAt = receivedAt,
    isRead = isRead,
    isAnonymous = isAnonymous,
    readAt = readAt,
    lastCursorId = lastCursorId,
)
