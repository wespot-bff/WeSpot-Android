package com.bff.wespot.data.remote.model.community

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val postId: Int,
    val content: String
)
