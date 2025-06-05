package com.bff.wespot.model.message.response

import java.time.LocalDateTime

/**
 * @property [senderProfile] 본인을 기준으로 senderProfile, 상대방이 receiverProfile로 설정된다.
 */
data class Message(
    val id: Int = -1,
    val senderProfile: MessageProfile = MessageProfile(),
    val receiverProfile: MessageProfile = MessageProfile(),
    val isExistsUnreadMessage: Boolean = false,
    val latestChatTime: LocalDateTime? = LocalDateTime.MIN,
    val isMeMessageRoomOwner: Boolean = false,
    val isBookmarked: Boolean = false,
    val isBlocked: Boolean = false,
    val isEver: Boolean = false,
)
