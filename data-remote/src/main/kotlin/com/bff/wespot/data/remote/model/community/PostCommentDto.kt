package com.bff.wespot.data.remote.model.community

import com.bff.wespot.model.community.PostComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostCommentDto(
    val id: String,
    val isMe: Boolean,
    @SerialName("authorName")
    val nickname: String,
    @SerialName("authorImage")
    val profileImage: String,
    @SerialName("content")
    val message: String,
    val createdAt: String,
    val likeCount: Int,
    @SerialName("hasPushedLike")
    val pushedLike: Boolean,
    val isReported: Boolean,
) {
    fun toDomain(): PostComment {
        return PostComment(
            id = id,
            isMe = isMe,
            nickname = nickname,
            profileImage = profileImage,
            message = message,
            createdAt = createdAt,
            likeCount = likeCount,
            pushedLike = pushedLike,
            isReported = isReported,
        )
    }
}