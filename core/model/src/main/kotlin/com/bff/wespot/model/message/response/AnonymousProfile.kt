package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class AnonymousProfile(
    val name: String = "",
    val imageUrl: String = "",
    val lastSentDate: LocalDateTime = LocalDateTime.MIN,
) {
    fun isEmpty() = name.isEmpty() && imageUrl.isEmpty()
}
