package com.bff.wespot.model.message.response

import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

/**
 * 보낸 쪽지, 예약된 쪽지, 쪽지 조회에서 사용되는 전체 필드를 가진 Model
 */
data class Message(
    override val id: Int,
    override val senderName: String,
    override val receiver: User,
    override val content: String,
    override val receivedAt: LocalDateTime?,
    override val isRead: Boolean,
    override val readAt: LocalDateTime?,
    val isReported: Boolean,
    val isBlocked: Boolean,
    val isAnonymous: Boolean,
) : BaseMessage(id, senderName, receiver, content, receivedAt, isRead, readAt) {
    constructor() : this(-1, "", User(), "", LocalDateTime.MAX, false, null, false, false, false)

    fun toReceivedMessage(): ReceivedMessage = ReceivedMessage(
        id = id,
        senderName = senderName,
        receiver = receiver,
        content = content,
        receivedAt = receivedAt,
        isRead = isRead,
        readAt = readAt,
    )
}
