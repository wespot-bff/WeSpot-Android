package com.bff.wespot.model.message.response

data class MessageContent(
    val receiver: String = "",
    val content: String = "",
    val sender: String = "",
)
