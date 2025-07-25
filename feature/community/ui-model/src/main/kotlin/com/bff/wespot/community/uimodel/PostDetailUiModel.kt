package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.PostDetail
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class PostDetailUiModel(
    val id: String,
    val content: PostDetailContentUiModel,
) {
    data class PostDetailContentUiModel(
        val category: CategoryUiModel,
        val headerSection: HeaderSectionUiModel,
        val infoSection: InfoSectionUiModel,
        val contentSection: ContentSectionUiModel?,
        val footerSection: FooterSectionUiModel,
    ) {
        data class CategoryUiModel(
            val text: RichTextType,
            val target: String,
            val icon: IconType,
        )

        data class HeaderSectionUiModel(
            val profileImage: String,
            val nickname: RichTextType,
            val createdAt: RichTextType,
            val button: ButtonUiModel,
        ) {
            data class ButtonUiModel(
                val type: String,
                val icon: IconType,
                val text: RichTextType,
            )
        }

        data class InfoSectionUiModel(
            val title: RichTextType,
            val description: RichTextType,
            val maxLine: Int,
        )

        sealed interface ContentSectionUiModel {
            data class ImagesContentUiModel(
                val content: List<ImageType>,
            ) : ContentSectionUiModel
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
                val count: RichTextType,
            )
        }
    }

    companion object {
        fun PostDetail.toUiModel() = PostDetailUiModel(
            id = id,
            content = content.toUiModel(),
        )

        val Empty = PostDetailUiModel(
            id = "",
            content = PostDetailContentUiModel(
                category = PostDetailContentUiModel.CategoryUiModel(
                    text = RichTextType(
                        text = "",
                        color = ColorType.Token(""),
                        typography = "",
                    ),
                    target = "",
                    icon = IconType(url = "", color = ColorType.Token("")),
                ),
                headerSection = PostDetailContentUiModel.HeaderSectionUiModel(
                    profileImage = "",
                    nickname = RichTextType(
                        text = "",
                        color = ColorType.Token(""),
                        typography = "",
                    ),
                    createdAt = RichTextType(
                        text = "",
                        color = ColorType.Token(""),
                        typography = "",
                    ),
                    button = PostDetailContentUiModel.HeaderSectionUiModel.ButtonUiModel(
                        type = "",
                        icon = IconType(url = "", color = ColorType.Token("")),
                        text = RichTextType(
                            text = "",
                            color = ColorType.Token(""),
                            typography = "",
                        ),
                    ),
                ),
                infoSection = PostDetailContentUiModel.InfoSectionUiModel(
                    title = RichTextType(
                        text = "",
                        color = ColorType.Token(""),
                        typography = "",
                    ),
                    description = RichTextType(
                        text = "",
                        color = ColorType.Token(""),
                        typography = "",
                    ),
                    maxLine = 0,
                ),
                contentSection = null,
                footerSection = PostDetailContentUiModel.FooterSectionUiModel(
                    reactions = emptyList(),
                    scrap = PostDetailContentUiModel.FooterSectionUiModel.ScrapUiModel(
                        icon = IconType(url = "", color = ColorType.Token("")),
                        count = RichTextType(
                            text = "",
                            color = ColorType.Token(""),
                            typography = "",
                        ),
                    ),
                ),
            ),
        )
    }
}

private fun PostDetail.PostDetailContent.toUiModel() = PostDetailUiModel.PostDetailContentUiModel(
    category = category.toUiModel(),
    headerSection = headerSection.toUiModel(),
    infoSection = infoSection.toUiModel(),
    contentSection = contentSection?.toUiModel(),
    footerSection = footerSection.toUiModel(),
)

private fun PostDetail.PostDetailContent.Category.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.CategoryUiModel(
        text = text,
        target = target,
        icon = icon,
    )

private fun PostDetail.PostDetailContent.HeaderSection.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.HeaderSectionUiModel(
        profileImage = profileImage,
        nickname = nickname,
        createdAt = createdAt,
        button = button.toUiModel(),
    )

private fun PostDetail.PostDetailContent.HeaderSection.Button.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.HeaderSectionUiModel.ButtonUiModel(
        type = type,
        icon = icon,
        text = text,
    )

private fun PostDetail.PostDetailContent.InfoSection.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.InfoSectionUiModel(
        title = title,
        description = description,
        maxLine = maxLine,
    )

private fun PostDetail.PostDetailContent.ContentSection.toUiModel(): PostDetailUiModel.PostDetailContentUiModel.ContentSectionUiModel =
    when (this) {
        is PostDetail.PostDetailContent.ContentSection.ImagesContent -> {
            this.toUiModel()
        }
    }

private fun PostDetail.PostDetailContent.ContentSection.ImagesContent.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.ContentSectionUiModel.ImagesContentUiModel(
        content = content,
    )

private fun PostDetail.PostDetailContent.FooterSection.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel(
        reactions = reactions.map { it.toUiModel() },
        scrap = scrap.toUiModel(),
    )

private fun PostDetail.PostDetailContent.FooterSection.Reaction.toUiModel() = when (this) {
    is PostDetail.PostDetailContent.FooterSection.Reaction.Chat -> {
        PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.ChatUiModel(
            icon = icon,
            count = count,
            selected = selected,
        )
    }

    is PostDetail.PostDetailContent.FooterSection.Reaction.Like -> {
        PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ReactionUiModel.LikeUiModel(
            icon = icon,
            count = count,
            selected = selected,
        )
    }
}

private fun PostDetail.PostDetailContent.FooterSection.Scrap.toUiModel() =
    PostDetailUiModel.PostDetailContentUiModel.FooterSectionUiModel.ScrapUiModel(
        icon = icon,
        count = count,
    )
