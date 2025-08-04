package com.bff.wespot.data.remote.model.community

import com.bff.wespot.data.remote.model.serverDriven.type.ColorTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.IconTypeDto
import com.bff.wespot.data.remote.model.serverDriven.type.RichTextTypeDto
import com.bff.wespot.model.community.VoteItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("VoteItem")
data class VoteItemDto(
    val id: String,
    val content: VoteContentDto
) : BaseCommunityContentDto {
    override fun toDomain() = VoteItem(
        id = id,
        content = content.toDomain()
    )
}

@Serializable
data class VoteContentDto(
    val badge: BadgeDto,
    val text: RichTextTypeDto,
    val actionIcon: ActionIconDto,
    val gradation: GradationDto
) {
    @Serializable
    data class BadgeDto(
        val backgroundColor: ColorTypeDto,
        val text: RichTextTypeDto
    )

    @Serializable
    data class ActionIconDto(
        val backgroundColor: ColorTypeDto,
        val icon: IconTypeDto
    )

    @Serializable
    data class GradationDto(
        val startColor: ColorTypeDto,
        val endColor: ColorTypeDto,
        val angle: Int
    )
}

private fun VoteContentDto.toDomain() = VoteItem.VoteContent(
    badge = badge.toDomain(),
    text = text.toDomain(),
    actionIcon = actionIcon.toDomain(),
    gradation = gradation.toDomain()
)

private fun VoteContentDto.BadgeDto.toDomain() = VoteItem.VoteContent.Badge(
    backgroundColor = backgroundColor.toDomain(),
    text = text.toDomain()
)

private fun VoteContentDto.ActionIconDto.toDomain() = VoteItem.VoteContent.ActionIcon(
    backgroundColor = backgroundColor.toDomain(),
    icon = icon.toDomain()
)

private fun VoteContentDto.GradationDto.toDomain() = VoteItem.VoteContent.Gradation(
    startColor = startColor.toDomain(),
    endColor = endColor.toDomain(),
    angle = angle
)