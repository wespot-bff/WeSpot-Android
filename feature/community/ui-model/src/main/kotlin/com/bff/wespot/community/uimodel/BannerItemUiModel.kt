package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.BannerItem
import com.bff.wespot.model.serverDriven.type.IconType
import com.bff.wespot.model.serverDriven.type.ImageType
import com.bff.wespot.model.serverDriven.type.RichTextType

data class BannerItemUiModel(
    override val id: String,
    val content: BannerContentUiModel,
) : BaseCommunityContentUiModel {
    data class BannerContentUiModel(
        val thumbnail: ImageType,
        val title: RichTextType,
        val description: RichTextType,
        val icon: IconType,
    )
}

fun BannerItem.toUiModel() = BannerItemUiModel(
    id = id,
    content = content.toUiModel(),
)

private fun BannerItem.BannerContent.toUiModel() =
    BannerItemUiModel.BannerContentUiModel(
        thumbnail = thumbnail,
        title = title,
        description = description,
        icon = icon,
    )
