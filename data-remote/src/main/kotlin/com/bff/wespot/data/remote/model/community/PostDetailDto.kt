package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.ImageTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.PostDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailDto(
    val id: String,
    val isMyPost: Boolean,
    val content: PostDetailContentDto,
) {
    @Serializable
    data class PostDetailContentDto(
        val category: CategoryDto,
        val headerSection: HeaderSectionDto,
        val infoSection: InfoSectionDto,
        val contentSection: ContentSectionDto? = null,
        val footerSection: FooterSectionDto,
    ) {
        @Serializable
        data class CategoryDto(
            val text: RichTextTypeDto,
            val target: String,
            val icon: IconTypeDto,
        )

        @Serializable
        data class HeaderSectionDto(
            val profileImage: ImageTypeDto,
            val nickname: RichTextTypeDto,
            val createdAt: RichTextTypeDto,
            val button: ButtonDto,
        ) {
            @Serializable
            data class ButtonDto(
                val type: String,
                val icon: IconTypeDto,
                val text: RichTextTypeDto,
                val isSelected: Boolean,
            )
        }

        @Serializable
        data class InfoSectionDto(
            val title: RichTextTypeDto?,
            val description: RichTextTypeDto,
            val maxLine: Int,
        )

        @Serializable
        sealed class ContentSectionDto {
            @Serializable
            @SerialName("Images")
            data class ImagesContentDto(
                val images: List<String>,
            ) : ContentSectionDto()
        }

        @Serializable
        data class FooterSectionDto(
            val reactions: List<ReactionDto>,
            val scrap: ScrapDto,
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

    fun toDomain(): PostDetail {
        return PostDetail(
            id = id,
            isMyPost = isMyPost,
            content = content.toDomain(),
        )
    }
}

private fun PostDetailDto.PostDetailContentDto.toDomain() = PostDetail.PostDetailContent(
    category = category.toDomain(),
    headerSection = headerSection.toDomain(),
    infoSection = infoSection.toDomain(),
    contentSection = contentSection?.toDomain(),
    footerSection = footerSection.toDomain(),
)


private fun PostDetailDto.PostDetailContentDto.CategoryDto.toDomain() =
    PostDetail.PostDetailContent.Category(
        text = text.toDomain(),
        target = target,
        icon = icon.toDomain(),
    )

private fun PostDetailDto.PostDetailContentDto.HeaderSectionDto.toDomain() =
    PostDetail.PostDetailContent.HeaderSection(
        profileImage = profileImage.url,
        nickname = nickname.toDomain(),
        createdAt = createdAt.toDomain(),
        button = button.toDomain(),
    )

private fun PostDetailDto.PostDetailContentDto.HeaderSectionDto.ButtonDto.toDomain() =
    PostDetail.PostDetailContent.HeaderSection.Button(
        type = type,
        icon = icon.toDomain(),
        text = text.toDomain(),
        isSelected = isSelected,
    )

private fun PostDetailDto.PostDetailContentDto.InfoSectionDto.toDomain() =
    PostDetail.PostDetailContent.InfoSection(
        title = title?.toDomain(),
        description = description.toDomain(),
        maxLine = maxLine,
    )

private fun PostDetailDto.PostDetailContentDto.ContentSectionDto.toDomain(): PostDetail.PostDetailContent.ContentSection =
    when (this) {
        is PostDetailDto.PostDetailContentDto.ContentSectionDto.ImagesContentDto -> {
            this.toDomain()
        }
    }

private fun PostDetailDto.PostDetailContentDto.ContentSectionDto.ImagesContentDto.toDomain() =
    PostDetail.PostDetailContent.ContentSection.ImagesContent(
        content = images
    )

private fun PostDetailDto.PostDetailContentDto.FooterSectionDto.toDomain() =
    PostDetail.PostDetailContent.FooterSection(
        reactions = reactions.map { it.toDomain() },
        scrap = scrap.toDomain(),
    )

private fun PostDetailDto.PostDetailContentDto.FooterSectionDto.ReactionDto.toDomain() =
    when (this) {
        is PostDetailDto.PostDetailContentDto.FooterSectionDto.ReactionDto.ChatDto -> {
            PostDetail.PostDetailContent.FooterSection.Reaction.Chat(
                icon = icon.toDomain(),
                count = count.toDomain(),
                selected = selected
            )
        }

        is PostDetailDto.PostDetailContentDto.FooterSectionDto.ReactionDto.LikeDto -> {
            PostDetail.PostDetailContent.FooterSection.Reaction.Like(
                icon = icon.toDomain(),
                count = count.toDomain(),
                selected = selected
            )
        }
    }

private fun PostDetailDto.PostDetailContentDto.FooterSectionDto.ScrapDto.toDomain(): PostDetail.PostDetailContent.FooterSection.Scrap {
    return PostDetail.PostDetailContent.FooterSection.Scrap(
        icon = icon.toDomain(),
        selected = selected,
    )
}