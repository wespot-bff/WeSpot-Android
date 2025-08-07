package com.bff.wespot.model.community

import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class PostDetail(
    val id: String,
    val content: PostDetailContent,
) {
    data class PostDetailContent(
        val category: Category,
        val headerSection: HeaderSection,
        val infoSection: InfoSection,
        val contentSection: ContentSection?,
        val footerSection: FooterSection,
    ) {
        data class Category(
            val text: RichTextType,
            val target: String,
            val icon: IconType,
        )

        data class HeaderSection(
            val profileImage: String,
            val nickname: RichTextType,
            val createdAt: RichTextType,
            val button: Button,
        ) {
            data class Button(
                val type: String,
                val icon: IconType,
                val text: RichTextType,
            )
        }

        data class InfoSection(
            val title: RichTextType?,
            val description: RichTextType,
            val maxLine: Int,
        )

        sealed class ContentSection {
            data class ImagesContent(
                val content: List<String>,
            ) : ContentSection()
        }

        data class FooterSection(
            val reactions: List<Reaction>,
            val scrap: Scrap,
        ) {
            sealed class Reaction(
                open val icon: IconType,
                open val count: RichTextType,
                open val selected: Boolean,
            ) {
                data class Chat(
                    override val icon: IconType,
                    override val count: RichTextType,
                    override val selected: Boolean,
                ) : Reaction(icon, count, selected)

                data class Like(
                    override val icon: IconType,
                    override val count: RichTextType,
                    override val selected: Boolean,
                ) : Reaction(icon, count, selected)
            }

            data class Scrap(
                val icon: IconType,
                val selected: Boolean,
            )
        }
    }
}
