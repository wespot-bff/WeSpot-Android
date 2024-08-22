package com.bff.wespot.data.remote.model.common

import com.bff.wespot.model.common.KakaoContent
import kotlinx.serialization.Serializable

@Serializable
data class KakaoContentDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val buttonText: String,
    val url: String,
) {
    fun toKakaoContent() = KakaoContent(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        buttonText = buttonText,
        url = url,
    )
}