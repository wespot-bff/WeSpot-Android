package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.VoteItem
import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.GradationType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class VoteItemUiModel(
    override val id: String,
    val content: VoteContentUiModel,
) : BaseCommunityContentUiModel {
    data class VoteContentUiModel(
        val badge: BadgeUiModel,
        val text: RichTextType,
        val actionIcon: ActionIconUiModel,
        val gradation: GradationType,
    ) {
        data class BadgeUiModel(
            val backgroundColor: ColorType,
            val text: RichTextType,
        )

        data class ActionIconUiModel(
            val backgroundColor: ColorType,
            val icon: IconType,
        )
    }
}

fun VoteItem.toUiModel() = VoteItemUiModel(
    id = id,
    content = content.toUiModel(),
)

private fun VoteItem.VoteContent.toUiModel() = VoteItemUiModel.VoteContentUiModel(
    badge = badge.toUiModel(),
    text = text,
    actionIcon = actionIcon.toUiModel(),
    gradation = gradation,
)

private fun VoteItem.VoteContent.Badge.toUiModel() =
    VoteItemUiModel.VoteContentUiModel.BadgeUiModel(
        backgroundColor = backgroundColor,
        text = text,
    )

private fun VoteItem.VoteContent.ActionIcon.toUiModel() =
    VoteItemUiModel.VoteContentUiModel.ActionIconUiModel(
        backgroundColor = backgroundColor,
        icon = icon,
    )
