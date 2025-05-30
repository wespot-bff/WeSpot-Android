package com.bff.wespot.data.mapper.message

import com.bff.wespot.data.local.model.message.MessageEntity
import com.bff.wespot.data.remote.model.message.request.SendMessageDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto
import com.bff.wespot.model.message.request.SendMessage

internal fun SendMessage.toDto(): SendMessageDto = SendMessageDto(
    receiverId = receiverId,
    content = content,
    isAnonymous = isAnonymous,
    anonymousImageUrl = anonymousImageUrl,
    anonymousProfileName = anonymousProfileName,
)

internal fun MessageListDto.toEntity(
    lastCursorId: Int?,
) = MessageEntityList(
    data = messages.map { it.toMessageEntity(lastCursorId) },
    lastCursorId = lastCursorId ?: -1,
    hasNext = hasNext,
)

private fun MessageDto.toMessageEntity(
    lastCursorId: Int?,
): MessageEntity = MessageEntity(
    id = id,
    senderProfile = senderProfile.toMessageProfile(),
    receiverProfile = receiverProfile.toMessageProfile(),
    isExistsUnreadMessage = isExistsUnreadMessage,
    latestChatTime = latestChatTime,
    isBookmarked = isBookmarked,
    isBlocked = isBlocked,
    isEver = isEver,
    lastCursorId = lastCursorId,
    isMeMessageRoomOwner = isMeMessageRoomOwner,
)
