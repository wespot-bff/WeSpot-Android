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
    val isReported: Boolean,
    val isBlocked: Boolean,
    val readAt: LocalDateTime?,
) {
    constructor() : this(-1, "", -1, "", "", -1, -1, "", LocalDateTime.MAX, false, false, false, null)

    fun toReceiverDescription() =
        "${receiverSchoolName.replace("중학교", "중").replace("고등학교", "고")} ${receiverGrade}학년 ${receiverClassNumber}반 $receiverName"
}
