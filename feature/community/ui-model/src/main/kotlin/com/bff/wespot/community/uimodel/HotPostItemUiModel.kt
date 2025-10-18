package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.HotPostItem
import com.bff.wespot.model.serverDriven.type.GradationType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class HotPostItemUiModel(
    override val id: String,
    val content: HotPostContentUiModel,
) : BaseCommunityContentUiModel {
    data class HotPostContentUiModel(
        val title: TitleUiModel,
        val posts: List<PostUiModel>,
    ) {
        data class TitleUiModel(
            val icon: IconType,
            val text: RichTextType,
        )

        data class PostUiModel(
            val targetId: String,
            val headerSection: HeaderSectionUiModel,
            val infoSection: InfoSectionUiModel,
            val createdAt: RichTextType,
            val gradation: GradationType,
        ) {
            data class HeaderSectionUiModel(
                val profileImage: ImageType,
                val nickname: RichTextType,
            )

            data class InfoSectionUiModel(
                val title: RichTextType?,
                val description: RichTextType,
            )
        }
    }
}

fun HotPostItem.toUiModel() = HotPostItemUiModel(
    id = id,
    content = content.toUiModel(),
)

private fun HotPostItem.HotPostContent.toUiModel() = HotPostItemUiModel.HotPostContentUiModel(
    title = title.toUiModel(),
    posts = posts.map { it.toUiModel() },
)

private fun HotPostItem.HotPostContent.Title.toUiModel() =
    HotPostItemUiModel.HotPostContentUiModel.TitleUiModel(
        icon = icon,
        text = text,
    )

private fun HotPostItem.HotPostContent.Post.toUiModel() =
    HotPostItemUiModel.HotPostContentUiModel.PostUiModel(
        targetId = targetId,
        headerSection = headerSection.toUiModel(),
        infoSection = infoSection.toUiModel(),
        createdAt = createdAt,
        gradation = gradation,
    )

private fun HotPostItem.HotPostContent.Post.HeaderSection.toUiModel() =
    HotPostItemUiModel.HotPostContentUiModel.PostUiModel.HeaderSectionUiModel(
        profileImage = profileImage,
        nickname = nickname,
    )

private fun HotPostItem.HotPostContent.Post.InfoSection.toUiModel() =
    HotPostItemUiModel.HotPostContentUiModel.PostUiModel.InfoSectionUiModel(
        title = title,
        description = description,
    )
