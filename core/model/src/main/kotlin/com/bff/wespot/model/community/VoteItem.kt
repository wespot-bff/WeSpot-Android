package com.bff.wespot.model.community

import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.GradationType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class VoteItem(
    override val id: String,
    val content: VoteContent,
) : BaseCommunityContent {
    data class VoteContent(
        val badge: Badge,
        val text: RichTextType,
        val actionIcon: ActionIcon,
        val gradation: GradationType,
    ) {
        data class Badge(
            val backgroundColor: ColorType,
            val text: RichTextType,
        )

        data class ActionIcon(
            val backgroundColor: ColorType,
            val icon: IconType,
        )
    }
}
