package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.PostItems
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class PostItemUiModel(
    override val id: String,
    val content: PostContentUiModel,
) : BaseCommunityContentUiModel {
    data class PostContentUiModel(
        val headerSection: HeaderSectionUiModel,
        val infoSection: InfoSectionUiModel,
        val contentSection: ContentSectionUiModel,
        val footerSection: FooterSectionUiModel,
    ) {
        data class HeaderSectionUiModel(
            val profileImage: String,
            val nickname: RichTextType,
            val createdAt: RichTextType,
            val category: CategoryUiModel,
        ) {
            data class CategoryUiModel(
                val text: RichTextType,
                val target: String,
                val icon: IconType,
            )
        }

        data class InfoSectionUiModel(
            val title: RichTextType,
            val description: RichTextType,
            val seeMore: RichTextType,
            val maxLine: Int,
        )

        sealed interface ContentSectionUiModel {
            data class ImagesSectionUiModel(
                val images: List<String>,
            ) : ContentSectionUiModel

            data class SingleImageUiModel(
                val image: String,
            ) : ContentSectionUiModel

            data object EmptySectionUiModel : ContentSectionUiModel
        }

        data class FooterSectionUiModel(
            val reactions: List<ReactionUiModel>,
            val scrap: ScrapUiModel,
        ) {
            sealed class ReactionUiModel(
                open val icon: IconType,
                open val count: RichTextType,
                open val selected: Boolean,
            ) {
                data class ChatUiModel(
                    override val icon: IconType,
                    override val count: RichTextType,
                    override val selected: Boolean,
                ) : ReactionUiModel(icon, count, selected)

                data class LikeUiModel(
                    override val icon: IconType,
                    override val count: RichTextType,
                    override val selected: Boolean,
                ) : ReactionUiModel(icon, count, selected)
            }

            data class ScrapUiModel(
                val icon: IconType,
                val selected: Boolean,
            )
        }
    }
}

fun PostItems.toUiModel() = PostItemUiModel(
    id = id,
    content = content.toUiModel(),
)

private fun PostItems.PostContent.toUiModel() =
    PostItemUiModel.PostContentUiModel(
        headerSection = headerSection.toUiModel(),
        infoSection = infoSection.toUiModel(),
        contentSection = contentSection.toUiModel(),
        footerSection = footerSection.toUiModel(),
    )

private fun PostItems.PostContent.HeaderSection.toUiModel() =
    PostItemUiModel.PostContentUiModel.HeaderSectionUiModel(
        profileImage = profileImage,
        nickname = nickname,
        createdAt = createdAt,
        category = category.toUiModel(),
    )

private fun PostItems.PostContent.HeaderSection.Category.toUiModel() =
    PostItemUiModel.PostContentUiModel.HeaderSectionUiModel.CategoryUiModel(
        text = text,
        target = target,
        icon = icon,
    )

private fun PostItems.PostContent.InfoSection.toUiModel() =
    PostItemUiModel.PostContentUiModel.InfoSectionUiModel(
        title = title,
        description = description,
        seeMore = seeMore,
        maxLine = maxLine,
    )

private fun PostItems.PostContent.ContentSection.toUiModel() =
    when (this) {
        is PostItems.PostContent.ContentSection.ImagesSection -> {
            if (images.size == 1) {
                PostItemUiModel.PostContentUiModel.ContentSectionUiModel.SingleImageUiModel(
                    image = images.first(),
                )
            } else {
                PostItemUiModel.PostContentUiModel.ContentSectionUiModel.ImagesSectionUiModel(
                    images = images,
                )
            }
        }

        PostItems.PostContent.ContentSection.EmptySection -> {
            PostItemUiModel.PostContentUiModel.ContentSectionUiModel.EmptySectionUiModel
        }
    }

private fun PostItems.PostContent.FooterSection.toUiModel() =
    PostItemUiModel.PostContentUiModel.FooterSectionUiModel(
        reactions = reactions.map { it.toUiModel() },
        scrap = scrap.toUiModel(),
    )

private fun PostItems.PostContent.FooterSection.Reaction.toUiModel() =
    when (this) {
        is PostItems.PostContent.FooterSection.Reaction.Chat -> {
            PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel(
                icon = icon,
                count = count,
                selected = selected,
            )
        }

        is PostItems.PostContent.FooterSection.Reaction.Like -> {
            PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel(
                icon = icon,
                count = count,
                selected = selected,
            )
        }
    }

private fun PostItems.PostContent.FooterSection.Scrap.toUiModel() =
    PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ScrapUiModel(
        icon = icon,
        selected = selected,
    )
