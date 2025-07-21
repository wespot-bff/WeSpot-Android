package com.bff.wespot.data.remote.model.community

import com.bff.wespot.model.community.BaseCommunityContent
import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseCommunityContentDto {
    fun toDomain(): BaseCommunityContent
}