package com.bff.wespot.data.remote.model.community

import com.bff.wespot.model.community.PostInfo
import kotlinx.serialization.Serializable

@Serializable
data class PostInfoDto(
    val categoryId: String,
    val title: String,
    val description: String,
    val imagesRequest: List<String>,
)

fun PostInfo.toDto() = PostInfoDto(
    categoryId = categoryId,
    title = title,
    description = description,
    imagesRequest = imagesRequest,
)