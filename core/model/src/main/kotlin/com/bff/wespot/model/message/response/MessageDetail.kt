package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

/**
 * 예약된 쪽지, 쪽지 조회에서 사용되는 전체 필드를 가진 Model
 */
data class MessageDetail(
    val id: Int = -1,
    val senderName: String = "",
    val sender: User = User(),
    val receiver: User = User(),
    val content: String = "",
    val receivedAt: LocalDateTime? = null,
    val isRead: Boolean = false,
    val readAt: LocalDateTime? = null,
    val isReported: Boolean = false,
    val isBlocked: Boolean = false,
    val isAnonymous: Boolean = false,
    val isFavorites: Boolean = false,
)
