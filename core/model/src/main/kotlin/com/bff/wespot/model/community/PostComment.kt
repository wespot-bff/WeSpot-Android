package com.bff.wespot.model.community

data class PostComment(
    val isMe: Boolean,
    val nickname: String,
    val profileImage: String,
    val message: String,
    val createdAt: String,
    val likeCount: Int,
)
