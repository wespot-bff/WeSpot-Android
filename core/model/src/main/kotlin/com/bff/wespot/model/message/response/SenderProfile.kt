package com.bff.wespot.model.message.response

import java.time.LocalDateTime

data class SenderProfile(
    val id: Int = -1,
    val name: String = "",
    val image: String = "",
    val recentlyTalk: LocalDateTime = LocalDateTime.MIN,
    val myTurnToAnswer: Boolean = false,
    val isAnonymous: Boolean = false,
)
