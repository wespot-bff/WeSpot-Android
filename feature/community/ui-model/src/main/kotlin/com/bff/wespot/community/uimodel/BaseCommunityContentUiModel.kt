package com.bff.wespot.community.uimodel

import com.bff.wespot.model.community.BannerItem
import com.bff.wespot.model.community.BaseCommunityContent
import com.bff.wespot.model.community.HotPostItem
import com.bff.wespot.model.community.PostItems
import com.bff.wespot.model.community.VoteItem

sealed interface BaseCommunityContentUiModel {
    val id: String
}

fun BaseCommunityContent.toUiModel() = when (this) {
    is PostItems -> this.toUiModel()
    is VoteItem -> this.toUiModel()
    is BannerItem -> this.toUiModel()
    is HotPostItem -> this.toUiModel()
    else -> UnKnownUiModel(id)
}
