package com.bff.wespot.community.all.state

import com.bff.wespot.community.uimodel.PostItemUiModel

sealed interface CommunityAllAction {
    data class LoadPostsByMenuType(val menu: String) : CommunityAllAction
    data class NavigateToDetail(val id: String) : CommunityAllAction
    data object NavigateUp : CommunityAllAction
    data class OnReactionClick(
        val id: String,
        val reaction: PostItemUiModel.PostContentUiModel.FooterSectionUiModel.ReactionUiModel,
    ) : CommunityAllAction
    data class OnScrapClick(
        val id: String,
    ) : CommunityAllAction
}
