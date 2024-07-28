package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class Message(
    val id: Int,
    val senderName: String,
    val receiverId: Int,
    val receiverName: String,
    val receiverSchoolName: String,
    val receiverGrade: Int,
    val receiverClassNumber: Int,
    val content: String,
    val receivedAt: LocalDateTime?,
    val isRead: Boolean,
    val isBlocked: Boolean = false,
    val readAt: LocalDateTime?,
) {
    constructor() : this(-1, "", -1, "", "", -1, -1, "", LocalDateTime.MAX, false, false, null)
}
