package com.bff.wespot.model.message.request

/**
 * @property [anonymousImageUrl], [anonymousProfileName]은 [isAnonymous]가 true일 때 함께 전달한다.
 */
data class SendMessage(
    val receiverId: Int,
    val content: String,
    val isAnonymous: Boolean,
    val anonymousImageUrl: String,
    val anonymousProfileName: String,
)
