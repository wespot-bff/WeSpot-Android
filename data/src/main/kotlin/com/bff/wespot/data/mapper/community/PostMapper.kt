package com.bff.wespot.data.mapper.community

import com.bff.wespot.data.remote.model.community.PostInfoDto
import com.bff.wespot.model.community.PostInfo

fun PostInfo.toDto() = PostInfoDto(
    categoryId = categoryId,
    title = title,
    description = description,
    imagesRequest = imagesRequest,
)