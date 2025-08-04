package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.PostItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PostItem")
data class PostItemDto(
    val id: String,
    val content: PostContentDto
) : BaseCommunityContentDto {
    override fun toDomain() = PostItems(
        id = id,
        content = content.toDomain()
    )
}

@Serializable
data class PostContentDto(
    val headerSection: HeaderSectionDto,
    val infoSection: InfoSectionDto,
    val contentSection: ContentSectionDto?,
    val footerSection: FooterSectionDto,
) {
    @Serializable
    data class HeaderSectionDto(
        val profileImage: String,
        val nickname: RichTextTypeDto,
        val createdAt: RichTextTypeDto,
        val category: CategoryDto
    ) {
        @Serializable
        data class CategoryDto(
            val text: RichTextTypeDto,
            val target: String,
            val icon: IconTypeDto
        )
    }

    @Serializable
    data class InfoSectionDto(
        val title: RichTextTypeDto,
        val description: RichTextTypeDto,
        val seeMore: RichTextTypeDto,
        val maxLine: Int
    )

    @Serializable
    sealed class ContentSectionDto {
        @Serializable
        @SerialName("Images")
        data class ImagesSectionDto(
            @SerialName("content")
            val images: List<ImageTypeDto>
        ) : ContentSectionDto()
    }

    @Serializable
    data class FooterSectionDto(
        val reactions: List<ReactionDto>,
        val scrap: ScrapDto
    ) {
        @Serializable
        sealed class ReactionDto {
            abstract val icon: IconTypeDto
            abstract val count: RichTextTypeDto
            abstract val selected: Boolean

            @Serializable
            @SerialName("Chat")
            data class ChatDto(
                override val icon: IconTypeDto,
                override val count: RichTextTypeDto,
                override val selected: Boolean
            ) : ReactionDto()

            @Serializable
            @SerialName("Like")
            data class LikeDto(
                override val icon: IconTypeDto,
                override val count: RichTextTypeDto,
                override val selected: Boolean
            ) : ReactionDto()
        }

        @Serializable
        data class ScrapDto(
            val icon: IconTypeDto,
            val selected: Boolean,
        )
    }
}

private fun PostContentDto.toDomain() = PostItems.PostContent(
    headerSection = headerSection.toDomain(),
    infoSection = infoSection.toDomain(),
    contentSection = contentSection?.toDomain()
        ?: PostItems.PostContent.ContentSection.EmptySection,
    footerSection = footerSection.toDomain()
)

private fun PostContentDto.HeaderSectionDto.toDomain() = PostItems.PostContent.HeaderSection(
    profileImage = profileImage,
    nickname = nickname.toDomain(),
    createdAt = createdAt.toDomain(),
    category = category.toDomain()
)

private fun PostContentDto.HeaderSectionDto.CategoryDto.toDomain() =
    PostItems.PostContent.HeaderSection.Category(
        text = text.toDomain(),
        target = target,
        icon = icon.toDomain()
    )

private fun PostContentDto.InfoSectionDto.toDomain() = PostItems.PostContent.InfoSection(
    title = title.toDomain(),
    description = description.toDomain(),
    seeMore = seeMore.toDomain(),
    maxLine = maxLine
)

private fun PostContentDto.ContentSectionDto.toDomain(): PostItems.PostContent.ContentSection =
    when (this) {
        is PostContentDto.ContentSectionDto.ImagesSectionDto -> PostItems.PostContent.ContentSection.ImagesSection(
            images = images.map { it.toDomain() }
        )
    }

private fun PostContentDto.FooterSectionDto.toDomain() = PostItems.PostContent.FooterSection(
    reactions = reactions.map { it.toDomain() },
    scrap = scrap.toDomain(),
)

private fun PostContentDto.FooterSectionDto.ReactionDto.toDomain(): PostItems.PostContent.FooterSection.Reaction =
    when (this) {
        is PostContentDto.FooterSectionDto.ReactionDto.ChatDto -> PostItems.PostContent.FooterSection.Reaction.Chat(
            icon = icon.toDomain(),
            count = count.toDomain(),
            selected = selected
        )

        is PostContentDto.FooterSectionDto.ReactionDto.LikeDto -> PostItems.PostContent.FooterSection.Reaction.Like(
            icon = icon.toDomain(),
            count = count.toDomain(),
            selected = selected
        )
    }

private fun PostContentDto.FooterSectionDto.ScrapDto.toDomain() =
    PostItems.PostContent.FooterSection.Scrap(
        icon = icon.toDomain(),
        selected = selected,
    )