package com.bff.wespot.model.community

import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class BannerItem(
    override val id: String,
    val content: BannerContent,
) : BaseCommunityContent {
    data class BannerContent(
        val thumbnail: ImageType,
        val title: RichTextType,
        val description: RichTextType,
        val icon: IconType,
    )
}
