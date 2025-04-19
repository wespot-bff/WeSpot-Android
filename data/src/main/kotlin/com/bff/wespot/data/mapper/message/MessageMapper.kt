package com.bff.wespot.data.mapper.message

import com.bff.wespot.data.local.model.message.MessageEntity
import com.bff.wespot.data.local.model.message.MessageEntityList
import com.bff.wespot.model.message.request.WrittenMessage
import com.bff.wespot.data.remote.model.message.request.WrittenMessageDto
import com.bff.wespot.data.remote.model.message.response.MessageDto
import com.bff.wespot.data.remote.model.message.response.MessageListDto

internal fun WrittenMessage.toWrittenMessageDto(): WrittenMessageDto = WrittenMessageDto(
    receiverId = receiverId,
    content = content,
    senderName = senderName,
    isAnonymous = isAnonymous,
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
    thumbnail = thumbnail,
    isExistsUnreadMessage = isExistsUnreadMessage,
    latestChatTime = latestChatTime,
    isAnonymous = isAnonymous,
    name = name,
    schoolName = schoolName,
    grade = grade,
    classNumber = classNumber,
    isBookmarked = isBookmarked,
    isReported = isReported,
    isBlocked = isBlocked,
    isEver = isEver,
    lastCursorId = lastCursorId,
)
