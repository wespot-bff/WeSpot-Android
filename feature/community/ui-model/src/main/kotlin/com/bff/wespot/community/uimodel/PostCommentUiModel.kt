package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.PostComment

data class PostCommentUiModel(
    val isMe: Boolean,
    val nickname: String,
    val profileImage: String,
    val message: String,
    val createdAt: String,
    val likeCount: Int,
) {
    companion object {
        fun PostComment.toUiModel() = PostCommentUiModel(
            isMe = isMe,
            nickname = nickname,
            profileImage = profileImage,
            message = message,
            createdAt = createdAt,
            likeCount = likeCount,
        )
    }
}
