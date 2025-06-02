package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class MessageDetail(
    val id: Int = -1,
    val createdAt: LocalDateTime? = LocalDateTime.MIN,
    val content: String = "",
    val isReceived: Boolean = true,
    val isSend: Boolean = false,
    val isRead: Boolean = false,
    val isAbleToAnswer: Boolean = false,
)
