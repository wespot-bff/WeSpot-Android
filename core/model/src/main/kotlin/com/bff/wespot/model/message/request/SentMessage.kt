package com.bff.wespot.model.message.request

data class SentMessage(
    val receivedId: Int,
    val schoolId: Int,
    val grade: Int,
    val group: Int,
    val content: String,
    val sender: String,
)
