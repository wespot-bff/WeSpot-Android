package com.bff.wespot.model.message.response

import com.bff.wespot.model.message.Sender
import com.bff.wespot.model.user.response.User
import java.time.LocalDateTime

data class BlockedMessage(
    val id: Int,
    val senderName: String,
    val senderProfile: Sender,
    val receiver: User,
    val content: String,
    val receivedAt: LocalDateTime?,
) {
    constructor() : this(-1, "", Sender(), User(), "", LocalDateTime.MIN)
}
