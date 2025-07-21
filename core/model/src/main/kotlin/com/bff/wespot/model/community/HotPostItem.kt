package com.bff.wespot.model.community

import com.bff.wespot.model.serverDriven.type.ColorType
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class HotPostItem(
    override val id: String,
    val content: HotPostContent,
) : BaseCommunityContent {
    data class HotPostContent(
        val title: Title,
        val posts: List<Post>,
    ) {
        data class Title(
            val icon: IconType,
            val text: RichTextType,
        )

        data class Post(
            val headerSection: HeaderSection,
            val infoSection: InfoSection,
            val createdAt: RichTextType,
            val gradation: Gradation,
        ) {
            data class HeaderSection(
                val profileImage: ImageType,
                val nickname: RichTextType,
            )

            data class InfoSection(
                val title: RichTextType,
                val description: RichTextType,
            )

            data class Gradation(
                val startColor: ColorType,
                val endColor: ColorType,
                val angle: Int,
            )
        }
    }
}
