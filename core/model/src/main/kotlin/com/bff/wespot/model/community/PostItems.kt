package com.bff.wespot.model.community

import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class PostItems(
    override val id: String,
    val content: PostContent,
) : BaseCommunityContent {
    data class PostContent(
        val headerSection: HeaderSection,
        val infoSection: InfoSection,
        val contentSection: ContentSection,
        val footerSection: FooterSection,
    ) {
        data class HeaderSection(
            val profileImage: String,
            val nickname: RichTextType,
            val createdAt: RichTextType,
            val category: Category?,
        ) {
            data class Category(
                val text: RichTextType,
                val target: String,
                val icon: IconType,
            )
        }

        data class InfoSection(
            val title: RichTextType?,
            val description: RichTextType,
            val seeMore: RichTextType,
            val maxLine: Int,
        )

        sealed interface ContentSection {
            data class ImagesSection(
                val images: List<String>,
            ) : ContentSection

            data object EmptySection : ContentSection
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
