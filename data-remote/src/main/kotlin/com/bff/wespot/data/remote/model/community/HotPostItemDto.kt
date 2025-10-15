package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.ColorTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.HotPostItem
import com.bff.wespot.model.serverDriven.type.GradationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("HotPostItem")
data class HotPostItemDto(
    val id: String,
    val content: HotPostContentDto
) : BaseCommunityContentDto {
    override fun toDomain() = HotPostItem(
        id = id,
        content = content.toDomain()
    )
}

@Serializable
data class HotPostContentDto(
    val title: TitleDto,
    val posts: List<PostDto>
) {
    @Serializable
    data class TitleDto(
        val icon: IconTypeDto,
        val text: RichTextTypeDto
    )

    @Serializable
    data class PostDto(
        val headerSection: HeaderSectionDto,
        val infoSection: InfoSectionDto,
        val createdAt: RichTextTypeDto,
        val gradation: GradationDto
    ) {
        @Serializable
        data class HeaderSectionDto(
            val profileImage: ImageTypeDto,
            val nickname: RichTextTypeDto
        )

        @Serializable
        data class InfoSectionDto(
            val title: RichTextTypeDto,
            val description: RichTextTypeDto
        )

        @Serializable
        data class GradationDto(
            val startColor: ColorTypeDto,
            val endColor: ColorTypeDto,
            val angle: Int
        )
    }
}

private fun HotPostContentDto.toDomain() = HotPostItem.HotPostContent(
    title = title.toDomain(),
    posts = posts.map { it.toDomain() }
)

private fun HotPostContentDto.TitleDto.toDomain() = HotPostItem.HotPostContent.Title(
    icon = icon.toDomain(),
    text = text.toDomain()
)

private fun HotPostContentDto.PostDto.toDomain() = HotPostItem.HotPostContent.Post(
    headerSection = headerSection.toDomain(),
    infoSection = infoSection.toDomain(),
    createdAt = createdAt.toDomain(),
    gradation = gradation.toDomain()
)

private fun HotPostContentDto.PostDto.HeaderSectionDto.toDomain() =
    HotPostItem.HotPostContent.Post.HeaderSection(
        profileImage = profileImage.toDomain(),
        nickname = nickname.toDomain()
    )

private fun HotPostContentDto.PostDto.InfoSectionDto.toDomain() =
    HotPostItem.HotPostContent.Post.InfoSection(
        title = title.toDomain(),
        description = description.toDomain()
    )

private fun HotPostContentDto.PostDto.GradationDto.toDomain() = GradationType(
    startColor = startColor.toDomain(),
    endColor = endColor.toDomain(),
    angle = angle
)