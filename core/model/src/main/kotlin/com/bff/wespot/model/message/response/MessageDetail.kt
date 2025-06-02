package com.bff.wespot.model.message.response

import java.time.LocalDateTime

/**
 * @property isAbleToAnswer : 마지막 쪽지가 받은 쪽지이면서, [MessageStatus.countRemainingMessages] > 0일 때 true
 *
 */
data class MessageDetail(
    val id: Int = -1,
    val createdAt: LocalDateTime? = LocalDateTime.MIN,
    val content: String = "",
    val isReceived: Boolean = true,
    val isSend: Boolean = false,
    val isRead: Boolean = false,
    val isAbleToAnswer: Boolean = false,
)
