package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.model.community.PostComment
import kotlinx.serialization.Serializable

@Serializable
data class PostCommentDto(
    val isMe: Boolean,
    val nickname: String,
    val profileImage: ImageTypeDto,
    val message: String,
    val createdAt: String,
    val likeCount: Int,
) {
    fun toDomain(): PostComment {
        return PostComment(
            isMe = isMe,
            nickname = nickname,
            profileImage = profileImage.url,
            message = message,
            createdAt = createdAt,
            likeCount = likeCount,
        )
    }
}