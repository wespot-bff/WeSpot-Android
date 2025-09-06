package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.BannerItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BannerItem")
data class BannerItemDto(
    val id: String,
    val content: BannerContentDto
) : BaseCommunityContentDto {
    override fun toDomain() = BannerItem(
        id = id,
        content = content.toDomain()
    )
}

@Serializable
data class BannerContentDto(
    val thumbnail: ImageTypeDto,
    val title: RichTextTypeDto,
    val description: RichTextTypeDto,
    val icon: IconTypeDto
)

private fun BannerContentDto.toDomain() = BannerItem.BannerContent(
    thumbnail = thumbnail.toDomain(),
    title = title.toDomain(),
    description = description.toDomain(),
    icon = icon.toDomain()
)